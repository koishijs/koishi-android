package cn.anillc.koishi

import kotlinx.coroutines.Runnable
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val pidRegex = Regex("^__PID__: (\\d+)$")
val statusRegex = Regex("^__STATUS__: (\\d+)$")

open class Proot(
    command: String,
    private val home: String,
    private val envPath: String,
    private val env: Map<String, String> = mapOf(),
) : Runnable {
    sealed class Status {
        data object Starting : Status()
        data class Running(val pid: Int) : Status()
        data class Exited(val status: Int) : Status()
        data object Stopping : Status()
        data object ForceExited : Status()
    }

    private val lock = ReentrantLock()
    private var process: Process? = null
    private var status: Status = Status.Starting
    private val condition = condition()

    init {
        val cmd = """
            setsid sh <<PROOT_EOF
            trap : SIGINT # to get status of process
            echo __PID__: \$$
            @PLACEHOLDER@
            echo __STATUS__: \$?
            echo -e '\n[Process exited.]\n\n'
            PROOT_EOF
        """.trimIndent().replace("@PLACEHOLDER@", command)
        lock.withLock {
            this.process = createProotProcess(cmd, home, envPath, env)
            Thread(this).start()
        }
    }

    override fun run() {
        val input = synchronized(this) { this.process!!.inputStream }
        input.bufferedReader().use {
            var starting = true
            for (line in it.lines()) {
                if (starting) {
                    val pid = pidRegex.matchEntire(line)
                    if (pid != null) {
                        synchronized(this@Proot) {
                            this.status = Status.Running(pid.groupValues[1].toInt())
                        }
                    }
                    starting = false
                    continue
                }
                val status = statusRegex.matchEntire(line)
                if (status != null) {
                    val code = status.groupValues[1].toInt()
                    lock.withLock {
                        this.status = Status.Exited(code)
                    }
                    condition.notifyAll()
                    onExit(code)
                    break
                }
                onData(line)
            }
        }
    }

    fun stop() {
        val pid = synchronized(this) {
            val status = this.status
            if (status is Status.Stopping) return
            if (status !is Status.Running) throw Exception("process is not running")
            this.status = Status.Stopping
            status.pid
        }
        val killProcess = createProotProcess(
            """
                ps -o pid,pgid | awk '{ if ($1 == "$pid") print "-" $2 }' | xargs kill -SIGINT
            """.trimIndent(), home, envPath
        )
        killProcess.waitFor()
        Thread {
            if (!condition.wait(10, TimeUnit.SECONDS)) {
                android.os.Process.sendSignal(this.process!!.pid(), android.os.Process.SIGNAL_KILL)
                this.status = Status.ForceExited
            }
        }.start()
    }

    fun status(): Status {
        return lock.withLock { this.status }
    }

    open fun onData(line: String) {}
    open fun onExit(status: Int?) {}
}

private fun createProotProcess(
    cmd: String,
    home: String,
    envPath: String,
    env: Map<String, String> = mapOf(),
): Process {
    val processBuilder = ProcessBuilder(
        "$fileDir/data/proot-static",
        "-r", "$fileDir/data${envPath}",
        "-b", "$fileDir/tmp:/tmp",
        "-b", "$fileDir/shm:/dev/shm",
        "-b", "$fileDir/data/nix:/nix",
        "-b", "$fileDir/data:/data",
        "-b", "$home:/home",
        "-b", "/proc:/proc",
        "-b", "/dev:/dev",
        "--sysvipc",
        "--link2symlink",
        "/bin/sh", "/bin/login", "-c", cmd
    ).redirectErrorStream(true)
    val environment = processBuilder.environment()
    environment.putAll(env)
    environment["PROOT_TMP_DIR"] = "$fileDir/tmp"
    return processBuilder.start()
}
