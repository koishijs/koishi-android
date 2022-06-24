package cn.anillc.koishi

import android.app.ProgressDialog
import android.system.Os
import android.util.Log
import io.vertx.kotlin.coroutines.await
import java.io.*
import java.lang.Exception
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

const val TAG = "Installer"

suspend fun install(activity: CoroutineActivity): String {
    val fileSystem = activity.vertx.fileSystem()

    val packageData = activity.filesDir.path
    val dataPath = "$packageData/data"
    val dataStagingPath = "$packageData/data-staging"

    if (fileSystem.exists(dataPath).await()) {
        return fileSystem.readFile("$packageData/env.txt").await().toString()
    }

    if (fileSystem.exists(dataStagingPath).await()) {
        activity.vertx.execute {
            deleteFolder(File(dataStagingPath))
        }
    }

    var envPath: String? = null

    var progress: ProgressDialog? = null
    activity.runOnUiThread {
        progress = ProgressDialog.show(activity, "test", "www")
    }

    activity.vertx.execute {
        var from: Reader? = null
        var to: Writer? = null
        try {
            from = activity.assets.open("bootstrap/env.txt").reader()
            to = FileWriter("$packageData/env.txt")
            val content = from.readText()
            to.write(content)
            envPath = content.trim()
        } finally {
            from?.close()
            to?.close()
        }
    }.await()

    activity.vertx.execute {
        val executables = arrayListOf<String>()
        val symlinks = arrayListOf<Pair<String, String>>()

        var zip: ZipInputStream? = null
        try {
            zip = ZipInputStream(activity.assets.open("bootstrap/bootstrap.zip"))
            var entry: ZipEntry?
            while (run { entry = zip.nextEntry; entry } != null) {
                val zipEntry = entry!!
                when (zipEntry.name) {
                    // readLine will closes stream
                    "EXECUTABLES.txt" -> executables.addAll(zip.reader()
                        .readText().split("\n").filter { it != "" })
                    "SYMLINKS.txt" -> symlinks.addAll(zip.reader()
                            .readText().split("\n").filter { it != "" }.map {
                        val (to, from) = it.split("←")
                        to to from
                    })
                    else -> {
                        val name = zipEntry.name
                        val file = File(dataStagingPath, name)
                        if (zipEntry.isDirectory) {
                            file.mkdirs()
                        } else {
                            file.parentFile.mkdirs()

                            FileOutputStream(file).use {
                                zip.copyTo(it)
                            }
                        }
                    }
                }
            }

            for (executable in executables) {
                try {
                    Os.chmod("$dataStagingPath/$executable", 700)
                } catch (e: Exception) {
                    Log.e(TAG, "install: failed to chmod: $executable", e)
                }
            }

            for (symlink in symlinks) {
                val (to, from) = symlink
                try {
                    Os.symlink(to, "$dataStagingPath/$from")
                } catch (e: Exception) {
                    Log.e(TAG, "install: failed to create symlink: $to ← $dataStagingPath/$from", e)
                }
            }

            if (!File(dataStagingPath).renameTo(File(dataPath))) {
                throw Exception("failed to rename staging")
            }
        } finally {
            zip?.close()
        }

    }.await()

    activity.runOnUiThread {
        progress?.hide()
    }

    return envPath!!
}

fun deleteFolder(file: File) {
    if (file.canonicalPath == file.absolutePath && file.isDirectory) {
        file.listFiles()?.forEach(::deleteFolder)
    }

    if (!file.delete()) {
        throw Exception("failed to delete $file")
    }
}