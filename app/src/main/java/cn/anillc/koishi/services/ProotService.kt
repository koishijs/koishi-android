package cn.anillc.koishi.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import cn.anillc.koishi.startProotProcess

open class ProotService : Service(), Runnable {
    class LocalBinder(
        val service: ProotService,
        var onInput: ((line: String) -> Unit)?,
    ) : Binder()

    private val binder by lazy { LocalBinder(this, null) }
    override fun onBind(intent: Intent?): IBinder = binder
    override fun onUnbind(intent: Intent?): Boolean {
        binder.onInput = null
        return true
    }

    private var process: Process? = null

    protected fun startProot(cmd: String, envPath: String) {
        if (process != null) return
        this.process = startProotProcess(cmd, filesDir.path, envPath)
        Thread(this).start()
    }

    protected fun stopProot() {
        val tProcess = process ?: return
        tProcess.destroy()
    }

    override fun run() {
        val reader = process?.inputStream?.bufferedReader() ?: return
        for (i in reader.lines()) {
            val onInput = binder.onInput ?: continue
            onInput(i)
        }
    }
}