package cn.anillc.koishi

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Process

class GoCqhttpService : Service() {
    class LocalBinder(val goCqhttpService: GoCqhttpService) : Binder()

    private val binder = LocalBinder(this)
    override fun onBind(intent: Intent?): IBinder = binder

    private var process: Process? = null

    fun startGoCqhttp() {

    }
}