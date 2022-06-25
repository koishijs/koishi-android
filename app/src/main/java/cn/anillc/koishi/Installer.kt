package cn.anillc.koishi

import android.content.Context
import android.system.Os
import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

const val TAG = "Installer"

fun getEnvPath(context: Context): String? {
    val dataPath = "${context.filesDir.path}/data"
    if(File(dataPath).exists()) {
        return FileReader("$dataPath/env.txt").use(FileReader::readText).trim()
    }
    return null
}

fun install(context: Context): String {
    val packageData = context.filesDir.path
    val dataPath = "$packageData/data"
    val dataStagingPath = "$packageData/data-staging"
    val homeFile = File("$packageData/home")

    if (!homeFile.exists() && !homeFile.mkdirs()) {
        throw Exception("cannot create home dir")
    }

    val dataStagingFile = File(dataStagingPath)
    if (dataStagingFile.exists()) {
        deleteFolder(dataStagingFile)
    }

    if (!dataStagingFile.mkdirs()) {
        throw Exception("cannot create data-staging folder")
    }

    val envPath: String?
    var envPathFrom: Reader? = null
    var envPathTo: Writer? = null
    try {
        envPathFrom = context.assets.open("bootstrap/env.txt").reader()
        envPathTo = FileWriter("$dataStagingPath/env.txt")
        val content = envPathFrom.readText()
        envPathTo.write(content)
        envPath = content
    } finally {
        envPathFrom?.close()
        envPathTo?.close()
    }

    val executables = arrayListOf<String>()
    val symlinks = arrayListOf<Pair<String, String>>()

    var zip: ZipInputStream? = null
    try {
        zip = ZipInputStream(context.assets.open("bootstrap/bootstrap.zip"))
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
                Os.chmod("$dataStagingPath/$executable", 448) // 0700
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

    if (!File("$dataPath/tmp").mkdir()) {
        throw Exception("failed to create tmp folder")
    }

    return envPath!!.trim()
}

fun deleteFolder(file: File) {
    if (file.canonicalPath == file.absolutePath && file.isDirectory) {
        file.listFiles()?.forEach(::deleteFolder)
    }

    if (!file.delete()) {
        throw Exception("failed to delete $file")
    }
}