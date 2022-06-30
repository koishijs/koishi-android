package cn.anillc.koishi

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun startProotProcess(
    cmd: String,
    packagePath: String,
    envPath: String,
    env: Map<String, String> = mapOf(),
): Process {
    val processBuilder = ProcessBuilder(
        "$packagePath/data/proot-static",
        "-r", "$packagePath/data${envPath}",
        "-b", "$packagePath/tmp:/tmp",
        "-b", "$packagePath/shm:/dev/shm",
        "-b", "$packagePath/data/nix:/nix",
        "-b", "$packagePath/data:/data",
        "-b", "$packagePath/home:/home",
        "-b", "/proc:/proc",
        "-b", "/dev:/dev",
        "--sysvipc",
        "--link2symlink",
        "/bin/sh", "/bin/login", "-c", cmd
    ).redirectErrorStream(true)
    val environment = processBuilder.environment()
    environment.putAll(env)
    environment["PROOT_TMP_DIR"] = "$packagePath/tmp"
    return processBuilder.start()
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