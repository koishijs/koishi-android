package cn.anillc.koishi

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.services.ProotService

class KoishiApplication : Application() {

    class KoishiServiceConnection : ServiceConnection {
        var koishiBinder: ProotService.LocalBinder? = null
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            when (name.className) {
                KoishiService::class.qualifiedName ->
                    koishiBinder = binder as ProotService.LocalBinder
                else -> throw Exception("unknown service")
            }
            Log.i(TAG, "onServiceConnected: ${KoishiService::class.qualifiedName}")
        }
        override fun onServiceDisconnected(name: ComponentName) = when (name.className) {
            KoishiService::class.qualifiedName -> koishiBinder = null
            else -> throw Exception("unknown service")
        }
    }

    lateinit var envPath: String
    val isEnvPathInitialized get() = ::envPath.isInitialized
    val serviceConnection = KoishiServiceConnection()

    override fun onCreate() {
        super.onCreate()
        val envPath = getEnvPath(this)
        if (envPath != null) {
            this.envPath = envPath
            onInitialized()
        }
    }

    // be called after envPath is set
    fun onInitialized() {
        bindService(
            Intent(this, KoishiService::class.java),
            serviceConnection, BIND_AUTO_CREATE)
    }
}