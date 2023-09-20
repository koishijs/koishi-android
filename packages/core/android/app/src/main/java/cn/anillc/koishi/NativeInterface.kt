package cn.anillc.koishi

import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "native")
class NativeInterface : Plugin() {
    @PluginMethod
    fun starting(call: PluginCall) {
        val application = KoishiApplication.application
        synchronized(application) {
            if (application.status == KoishiApplication.Status.Initialized) {
                call.resolve()
            } else {
                application.status = KoishiApplication.Status.Wait(call)
            }
        }
    }
}
