package cn.anillc.koishi

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.base.CoroutineActivity
import kotlinx.coroutines.launch

class MainActivity : CoroutineActivity(), ServiceConnection {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var koishiService: KoishiService? = null
    private lateinit var envPath: String

    override suspend fun onCreateSuspend(savedInstanceState: Bundle?) {
        runOnUiThread { setContentView(R.layout.main) }
        envPath = install(this)

        val intent = Intent(this, KoishiService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        koishiService = (binder as KoishiService.LocalBinder).service
        launch {
            koishiService!!.startKoishi(envPath)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        koishiService = null
    }
}