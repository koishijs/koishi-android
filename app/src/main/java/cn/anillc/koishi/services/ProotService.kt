package cn.anillc.koishi.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.startProotProcess
import java.lang.Exception

open class ProotService : Service(), Runnable {
    companion object {
        val TAG = this::class.simpleName
    }

    // TODO: onExit
    class LocalBinder(
        val service: ProotService,
        var onInput: ((line: String) -> Unit)?,
    ) : Binder()

    private val binder by lazy { LocalBinder(this, null) }
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
        binder.onInput = null
        val tProcess = process ?: return
        tProcess.destroy()
        process = null
    }

    override fun run() {
        val reader = process?.inputStream?.bufferedReader() ?: return
        try {
            for (i in reader.lines()) {
                val onInput = binder.onInput ?: continue
                onInput(i)
            }
        } catch (e: Exception) {
            Log.e(TAG, "run", e)
        }
    }
}