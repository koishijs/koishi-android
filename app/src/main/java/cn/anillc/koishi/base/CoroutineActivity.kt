package cn.anillc.koishi.base

import android.app.Activity
import android.os.Bundle
import cn.anillc.koishi.KoishiApplication
import io.vertx.core.Vertx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class CoroutineActivity : Activity(), CoroutineScope {

    private lateinit var activityCoroutineContext: CoroutineContext
    lateinit var vertx: Vertx private set
    lateinit var koishiApplication: KoishiApplication private set
    override val coroutineContext: CoroutineContext get() = activityCoroutineContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        koishiApplication = application as KoishiApplication
        vertx = koishiApplication.vertx
        activityCoroutineContext = koishiApplication.dispatcher

        launch {
            onCreateSuspend(savedInstanceState)
        }
    }
    
    open suspend fun onCreateSuspend(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}