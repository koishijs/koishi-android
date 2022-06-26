package cn.anillc.koishi.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.pid
import cn.anillc.koishi.startProotProcess
import java.io.InterruptedIOException
import java.lang.Exception

open class ProotService : Service(), Runnable {
    companion object {
        val TAG = this::class.simpleName
    }

    // TODO: onExit
    class LocalBinder(
        val service: ProotService,
        var onInput: ((line: String) -> Unit)?,
        var onExit: ((exitValue: Int) -> Unit)?,
    ) : Binder()

    private val binder by lazy { LocalBinder(this, null, null) }
    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    var process: Process? = null
        private set

    protected fun startProot(cmd: String) {
        if (process != null) return
        this.process =
            startProotProcess(cmd, filesDir.path, (application as KoishiApplication).envPath)
        Thread(this).start()
    }

    protected fun stopProot() {
        val tProcess = process ?: return
        tProcess.destroy()
        process = null
    }

    override fun run() {
        try {
            val tProcess = process ?: return
            val reader = tProcess.inputStream.bufferedReader()
            for (i in reader.lines()) {
                val onInput = binder.onInput ?: continue
                onInput(i)
            }
            tProcess.waitFor()
            val onExit = binder.onExit ?: return
            onExit(tProcess.exitValue())
        } catch (e: InterruptedIOException) {
            Log.e(TAG, "run", e)
        } catch (e: Exception) {
            throw e
        } finally {
            binder.onInput = null
            binder.onExit = null
        }
    }
}