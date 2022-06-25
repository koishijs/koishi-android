package cn.anillc.koishi.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.services.GoCqhttpService
import cn.anillc.koishi.services.ProotService

class GoCqhttpActivity : Activity() {

    private lateinit var koishiApplication: KoishiApplication
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koishiApplication = application as KoishiApplication
        setContentView(R.layout.go_cqhttp)
        tv = findViewById(R.id.go_cqhttp_tv)
    }

    private fun getBinder(): ProotService.LocalBinder? {
        val binder = koishiApplication.serviceConnection.goCqhttpBinder
        if (binder == null) {
            Toast.makeText(this, R.string.failed_to_get_service, Toast.LENGTH_LONG).show()
            return null
        }
        return binder
    }

    fun onStartGoCqhttp(view: View) {
        val binder = getBinder() ?: return
        binder.onInput = {
            runOnUiThread {
                val str = it.replace(Regex("\\e\\[[\\d;]*[^\\d;]"), "")
                tv.append("\n$str")
                (tv.parent as ScrollView).fullScroll(View.FOCUS_DOWN)
            }
        }
        (binder.service as GoCqhttpService).startGoCqhttp()
    }

    fun onStopGoCqhttp(view: View) {
        val binder = getBinder() ?: return
        binder.service as GoCqhttpService
        if (binder.service.process == null) return
        binder.service.stopGoCqhttp()
        runOnUiThread {
            tv.append("\n\n[Process exited.]\n")
        }
    }
}