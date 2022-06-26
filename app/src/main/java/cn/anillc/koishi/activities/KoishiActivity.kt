package cn.anillc.koishi.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.services.ProotService

class KoishiActivity : Activity() {

    private lateinit var koishiApplication: KoishiApplication
    private lateinit var tv: TextView
    private lateinit var sv: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koishiApplication = application as KoishiApplication
        setContentView(R.layout.koishi)
        tv = findViewById(R.id.go_cqhttp_tv)
        sv = tv.parent as ScrollView
    }

    private fun getBinder(): ProotService.LocalBinder? {
        val binder = koishiApplication.serviceConnection.koishiBinder
        if (binder == null) {
            Toast.makeText(this, R.string.failed_to_get_service, Toast.LENGTH_LONG).show()
            return null
        }
        return binder
    }

    fun onStartKoishi(view: View) {
        val binder = getBinder() ?: return
        binder.onInput = {
            runOnUiThread {
                val text = it.replace(Regex("\\e\\[[\\d;]*[^\\d;]"), "")
                appendText("\n$text")
            }
        }
        binder.onExit = {
            runOnUiThread { appendText("\n\n[Process exited.]\n") }
        }
        (binder.service as KoishiService).startKoishi()
    }

    fun onStopKoishi(view: View) {
        val binder = getBinder() ?: return
        binder.service as KoishiService
        if (binder.service.process == null) return
        binder.service.stopKoishi()
    }

    override fun onDestroy() {
        super.onDestroy()
        val binder = getBinder() ?: return
        binder.onInput = null
    }

    fun appendText(text: String) {
        tv.append(text)
        sv.post { sv.fullScroll(View.FOCUS_DOWN) }
    }
}