package cn.anillc.koishi

import android.content.Context
import android.system.Os
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.concurrent.withLock

const val DEFAULT_DNS = "223.5.5.5"
const val DEFAULT_TIMEZONE = "Asia/Shanghai"
const val ABOUT =
    "Koishi Android v${BuildConfig.VERSION_NAME}\n项目开源于 GitHub koishijs/koishi-android"
const val TAG = "Koishi"


val fileDir: String by lazy {
    KoishiApplication.application.filesDir.path
}

val envPath: String by lazy {
    val env = KoishiApplication.application.assets.open("bootstrap/env.txt")
    env.reader().readText().trim()
}

fun init() {
    val files = arrayOf(
        File("$fileDir/home/instances"),
        File("$fileDir/tmp"),
        File("$fileDir/shm")
    )
    for (file in files) {
        if (!(file.exists() || file.mkdirs())) throw Exception("failed to create home")
    }
//    fun copyData(context: Context) {
//    unpackZip("bootstrap/bootstrap.zip", "data", context)
//    }

}

fun Process.pid(): Int {
    val clazz = this::class.java
    val pid = clazz.getDeclaredField("pid")
    pid.isAccessible = true
    return pid.get(this) as Int
}

fun String.removeVt100ControlChars(): String =
    replace(Regex("\\e\\[[\\d;]*[^\\d;]"), "")

fun condition(): Pair<Lock, Condition> {
    val lock = ReentrantLock()
    return lock to lock.newCondition()
}

fun Pair<Lock, Condition>.wait() = first.withLock(second::await)

fun Pair<Lock, Condition>.wait(times: Long, unit: TimeUnit) =
    first.withLock { second.await(times, unit) }

fun Pair<Lock, Condition>.notifyAll() = first.withLock(second::signalAll)

// File.delete fails on EMUI
fun File.rm(): Boolean {
    return Runtime.getRuntime().exec("rm -rf $absolutePath").waitFor() == 0
}

fun unpackZip(fileName: String, target: File, context: Context) {
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
                    val file = File(target, name)
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
                Os.chmod("${target.path}/$executable", 448) // 0700
            } catch (e: Exception) {
                Log.e(TAG, "install: failed to chmod: $executable", e)
            }
        }

        for (symlink in symlinks) {
            val (to, from) = symlink
            try {
                Os.symlink(to, "${target.path}/$from")
            } catch (e: Exception) {
                Log.e(TAG, "install: failed to create symlink: $to ← ${target.path}/$from", e)
            }
        }
    } finally {
        zip?.close()
    }
}
