package cn.anillc.koishi

import android.app.Application
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.dispatcher

class KoishiApplication : Application() {
    lateinit var vertx: Vertx
        private set
    val dispatcher get() = vertx.dispatcher()
    override fun onCreate() {
        super.onCreate()
        vertx = Vertx.vertx()
    }
}