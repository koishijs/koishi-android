package cn.anillc.koishi

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class KoishiService : Service() {
    companion object {
        private const val TAG = "KoishiService"
    }

    class LocalBinder(val service: KoishiService) : Binder()

    private val binder = LocalBinder(this)
    override fun onBind(intent: Intent?): IBinder = binder

    private var process: Process? = null

    fun startKoishi(envPath: String) {
        if (process != null) return
        val process = startProotProcess("go-cqhttp", filesDir.path, envPath)
        this.process = process
        Thread {
            val input = process.inputStream.bufferedReader()
            for (i in input.lines()) {
                Log.i(TAG, "startKoishi: $i")
            }
        }.start()
    }
}