package cn.anillc.koishi

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.getcapacitor.PluginCall

class KoishiApplication : Application() {
    companion object {
        lateinit var application: KoishiApplication
    }

    val serviceConnection = object : ServiceConnection {
        private var koishiBinder: KoishiService.LocalBinder? = null
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            when (name.className) {
                KoishiService::class.qualifiedName ->
                    koishiBinder = binder as KoishiService.LocalBinder

                else -> throw Exception("unknown service")
            }
        }

        override fun onServiceDisconnected(name: ComponentName) = when (name.className) {
            KoishiService::class.qualifiedName -> koishiBinder = null
            else -> throw Exception("unknown service")
        }
    }

    sealed class Status {
        data object Uninitialized : Status()
        data object Initialized : Status()
        data class Wait(val call: PluginCall) : Status()
    }
    var status: Status = Status.Uninitialized

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    fun onInitialized() {
//        bindService(
//            Intent(this, KoishiService::class.java),
//            serviceConnection, BIND_AUTO_CREATE
//        )
        synchronized(this) {
            val status = this.status
            if (status is Status.Uninitialized) {
                this.status = Status.Initialized
            } else if (status is Status.Wait) {
                status.call.resolve()
            }
        }
    }
}