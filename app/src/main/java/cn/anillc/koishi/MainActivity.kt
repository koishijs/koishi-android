package cn.anillc.koishi

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder

class MainActivity : Activity(), ServiceConnection {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var koishiService: KoishiService? = null
    private lateinit var envPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        Thread {
            envPath = install(this)

            val intent = Intent(this, KoishiService::class.java)
            bindService(intent, this, BIND_AUTO_CREATE)
        }.start()
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        koishiService = (binder as KoishiService.LocalBinder).service
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        koishiService = null
    }
}