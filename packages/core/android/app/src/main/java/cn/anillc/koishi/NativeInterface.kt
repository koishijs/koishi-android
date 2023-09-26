package cn.anillc.koishi

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "native")
class NativeInterface : Plugin() {
    private lateinit var preferences: SharedPreferences

    override fun handleOnStart() {
        super.handleOnStart()
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

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

    @PluginMethod
    fun startInstance(call: PluginCall) {
        val service = KoishiApplication.application.serviceConnection.koishiBinder!!.service
        val name = call.data.getString("name")
        service.instances[name]!!.start()
        call.resolve()
    }

    @PluginMethod
    fun stopInstance(call: PluginCall) {
        val service = KoishiApplication.application.serviceConnection.koishiBinder!!.service
        val name = call.data.getString("name")
        service.instances[name]!!.stop()
        call.resolve()
    }

    @PluginMethod
    fun getPreferenceString(call: PluginCall) {
        val value = preferences.getString(call.data.getString("key"), call.data.getString("default"))
        val res = JSObject()
        res.put("value", value)
        call.resolve(res)
    }

    @PluginMethod
    fun getPreferenceBoolean(call: PluginCall) {
        val value = preferences.getBoolean(call.data.getString("key"), call.data.getBoolean("default"))
        val res = JSObject()
        res.put("value", value)
        call.resolve(res)
    }

    @PluginMethod
    fun setPreferenceString(call: PluginCall) {
        with(preferences.edit()) {
            putString(call.data.getString("key"), call.data.getString("value"))
            apply()
        }
    }

    @PluginMethod
    fun setPreferenceBoolean(call: PluginCall) {
        with(preferences.edit()) {
            putBoolean(call.data.getString("key"), call.data.getBoolean("value"))
            apply()
        }
    }
}
