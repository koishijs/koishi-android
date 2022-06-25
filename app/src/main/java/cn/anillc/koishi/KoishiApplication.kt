package cn.anillc.koishi

import android.app.Application

class KoishiApplication : Application() {
    lateinit var envPath: String
    val isEnvPathInitialized get() = ::envPath.isInitialized

    override fun onCreate() {
        super.onCreate()
        val envPath = getEnvPath(this)
        if (envPath != null) this.envPath = envPath
    }
}