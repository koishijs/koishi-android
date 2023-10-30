package cn.anillc.koishi

import android.content.SharedPreferences
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import java.io.File

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
            instance.put("link", v.link)
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
    fun createInstance(call: PluginCall) {
        val service = KoishiApplication.application.serviceConnection.koishiBinder!!.service
        val name = call.data.getString("name")!!
        val instance = service.instances[name]
        val res = JSObject()
        if (instance != null) {
            res.put("value", false)
            call.resolve(res)
            return
        }
        service.instances[name] = Instance(name, context)
        res.put("value", true)
    }

    @PluginMethod
    fun removeInstance(call: PluginCall) {
        val service = KoishiApplication.application.serviceConnection.koishiBinder!!.service
        val name = call.data.getString("name")!!
        val instance = service.instances[name]
        instance!!.stop()
        val file = File("$fileDir/home/instances/$name")
        if (file.rm()) {
            service.instances.remove(name)
        }
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

    @PluginMethod
    fun toast(call: PluginCall) {
        Toast.makeText(context, call.getString("value"), Toast.LENGTH_LONG).show()
        call.resolve()
    }
}
