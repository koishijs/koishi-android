package cn.anillc.koishi

import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
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

    @PluginMethod
    fun instances(call: PluginCall) {
        // TODO: will binder be null?
        val service = KoishiApplication.application.serviceConnection.koishiBinder!!.service
        val result = JSObject()
        result.put("value", JSArray(service.instances.map { (k, v) ->
            val instance = JSObject()
            instance.put("name", k)
            instance.put("status", when (v.status()) {
                is Proot.Status.Starting, is Proot.Status.Running -> "Running"
                else  -> "Stopped"
            })
            instance
        }))
        call.resolve(result)
    }
}
