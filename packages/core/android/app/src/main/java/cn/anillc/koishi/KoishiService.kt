package cn.anillc.koishi

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.io.File

class KoishiService : Service() {
    class LocalBinder(
        val service: KoishiService,
    ) : Binder()
    private val binder by lazy { LocalBinder(this) }
    override fun onBind(intent: Intent?): IBinder = binder

    private val instances = mutableMapOf<String, Instance>()

    override fun onCreate() {
        super.onCreate()
        val instances = File("$fileDir/home/instances").list()
        if (instances!!.isEmpty()) {
            this.instances["default"] = Instance("default", this)
        } else {
            for (instance in instances) {
                this.instances[instance] = Instance(instance, this)
            }
        }
    }

}
