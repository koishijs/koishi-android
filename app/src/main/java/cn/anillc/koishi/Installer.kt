package cn.anillc.koishi

import android.content.Context
import android.system.Os
import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

const val TAG = "Installer"

fun getEnvPath(context: Context): String? {
    val envFile = "${context.filesDir.path}/env.txt"
    if (File(envFile).exists()) {
        return FileReader(envFile).use(FileReader::readText).trim()
    }
    return null
}

fun install(context: Context): String {
    val packagePath = context.filesDir.path
    val home = File("${packagePath}/home")
    if (!home.exists()) {
        if (!home.mkdirs()) throw Exception("failed to copy koishi.zip to home")
        val copyFile = { src: String, dst: String ->
            context.assets.open(src).use {
                FileOutputStream(dst).use(it::copyTo)
            }
        }
        copyFile("bootstrap/yarn.js", "$packagePath/home/yarn.js")
        copyFile("bootstrap/koishi.zip", "$packagePath/home/koishi.zip")
    }
    return copyData(context)
}

fun copyData(context: Context): String {
    val packageData = context.filesDir.path

    unpackZip("bootstrap/bootstrap.zip", "data", context)

    if (!File("$packageData/tmp").mkdir()) {
        throw Exception("failed to create tmp folder")
    }

    if (!File("$packageData/shm").mkdir()) {
        throw Exception("failed to create shm folder")
    }

    val envPath: String?
    var envPathFrom: Reader? = null
    var envPathTo: Writer? = null
    try {
        envPathFrom = context.assets.open("bootstrap/env.txt").reader()
        envPathTo = FileWriter("$packageData/env.txt")
        val content = envPathFrom.readText()
        envPathTo.write(content)
        envPath = content
    } finally {
        envPathFrom?.close()
        envPathTo?.close()
    }

    return envPath!!.trim()
}

fun unpackZip(fileName: String, target: String, context: Context) {
    val packagePath = context.filesDir.path
    val targetPath = "$packagePath/$target"
    val stagingPath = "$packagePath/$target-staging"
    val targetFile = File(targetPath)
    val stagingFile = File(stagingPath)

    if (stagingFile.exists() && !stagingFile.rm()) {
        throw Exception("cannot delete data-staging folder")
    }

    if (!stagingFile.mkdirs()) {
        throw Exception("cannot create data-staging folder")
    }

    val executables = arrayListOf<String>()
    val symlinks = arrayListOf<Pair<String, String>>()

    var zip: ZipInputStream? = null
    try {
        zip = ZipInputStream(context.assets.open(fileName))
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
                    val file = File(stagingPath, name)
                    if (zipEntry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile!!.mkdirs()

                        FileOutputStream(file).use {
                            zip.copyTo(it)
                        }
                    }
                }
            }
        }

        for (executable in executables) {
            try {
                Os.chmod("$stagingPath/$executable", 448) // 0700
            } catch (e: Exception) {
                Log.e(TAG, "install: failed to chmod: $executable", e)
            }
        }

        for (symlink in symlinks) {
            val (to, from) = symlink
            try {
                Os.symlink(to, "$stagingPath/$from")
            } catch (e: Exception) {
                Log.e(TAG, "install: failed to create symlink: $to ← $stagingPath/$from", e)
            }
        }

        if (!File(stagingPath).renameTo(targetFile)) {
            throw Exception("failed to rename data-staging")
        }
    } finally {
        zip?.close()
    }
}
