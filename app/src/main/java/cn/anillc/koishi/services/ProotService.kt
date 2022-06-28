package cn.anillc.koishi.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

open class ProotService : Service(), Runnable {
    companion object {
        val TAG = this::class.simpleName
        const val PROCESS_NOT_INITIALIZED = -1
        const val PROCESS_FORCE_KILLED = -2
    }

    class LocalBinder(
        val service: ProotService,
        var onInput: ((line: String) -> Unit)?,
        var onExit: ((exitValue: Int) -> Unit)?,
    ) : Binder()

    private lateinit var packagePath: String
    private lateinit var envPath: String

    private val binder by lazy { LocalBinder(this, null, null) }
    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    val process = AtomicReference<Process>()
    val log = AtomicReference("")
    val stopping = AtomicBoolean()
    private var pid = AtomicInteger(PROCESS_NOT_INITIALIZED)
    private var status = AtomicInteger(PROCESS_NOT_INITIALIZED)
    private var condition = condition()

    override fun onCreate() {
        super.onCreate()
        packagePath = filesDir.path
        envPath = (application as KoishiApplication).envPath
    }

    protected fun startProot(cmd: String, env: Map<String, String> = mapOf()) {
        if (process.get() != null) return
        this.process.set(
            startProotProcess(
                """
                    setsid sh -c "
                        trap : SIGINT # to get status of process
                        echo __PID__: $$
                        $cmd
                        echo __STATUS__: $?
                        echo -e '\n[Process exited.]\n\n'
                    "
                """.trimIndent(), packagePath, envPath, env
            )
        )
        Thread(this).start()
    }

    protected fun stopProot() {
        if (stopping.get()) return
        val process = this.process.get() ?: return
        stopping.set(true)
        val pid = this.pid.get()
        if (pid == PROCESS_NOT_INITIALIZED) {
            android.os.Process.sendSignal(process.pid(), android.os.Process.SIGNAL_KILL)
        } else {
            val killProcess = startProotProcess(
                """
                ps -o pid,pgid | awk '{ if ($1 == "$pid") print "-" $2 }' | xargs kill -SIGINT
            """.trimIndent(), packagePath, envPath
            )
            killProcess.waitFor()
            if (killProcess.exitValue() != 0) {
                killProcess.inputStream.bufferedReader().use {
                    Log.i(TAG, "stopProot: ${it.readText()}")
                }
            }
        }
        Thread {
                if (!condition.wait(10, TimeUnit.SECONDS)) {
                    android.os.Process.sendSignal(process.pid(), android.os.Process.SIGNAL_KILL)
                    status.set(PROCESS_FORCE_KILLED)
                }
        }.start()
    }

    override fun run() {
        try {
            val tProcess = process.get() ?: return
            val pidRegex = Regex("^__PID__: (\\d+)$")
            val statusRegex = Regex("^__STATUS__: (\\d+)$")
            val reader = tProcess.inputStream.bufferedReader()
            for (i in reader.lines()) {
                if (pid.get() == -1) {
                    val pidMatch = pidRegex.matchEntire(i)
                    if (pidMatch != null) {
                        pid.set(pidMatch.groupValues[1].toInt())
                        continue
                    }
                }
                if (status.get() == -1) {
                    val statusMatch = statusRegex.matchEntire(i)
                    if (statusMatch != null) {
                        status.set(statusMatch.groupValues[1].toInt())
                        condition.notifyAll()
                        continue
                    }
                }
                log.set("${log.get()}\n$i")
                onInput(i)
                val onBinderInput = binder.onInput ?: continue
                onBinderInput(i)
            }
            tProcess.waitFor()
            onExit(status.get())
            val onBinderExit = binder.onExit ?: return
            onBinderExit(status.get())
        } catch (e: Exception) {
            throw e
        } finally {
            process.set(null)
            binder.onInput = null
            binder.onExit = null
            pid.set(PROCESS_NOT_INITIALIZED)
            status.set(PROCESS_NOT_INITIALIZED)
            stopping.set(false)
        }
    }

    protected open fun onInput(line: String) {}
    protected open fun onExit(code: Int) {}
}