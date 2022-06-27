package cn.anillc.koishi.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.services.ProotService

class KoishiActivity : Activity() {

    companion object {
        val TAG = this::class.simpleName
    }

    private lateinit var koishiApplication: KoishiApplication
    private lateinit var tv: TextView
    private lateinit var sv: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koishiApplication = application as KoishiApplication
        setContentView(R.layout.koishi)
        tv = findViewById(R.id.go_cqhttp_tv)
        sv = tv.parent as ScrollView

        val binder = getBinder()
        if (binder == null) {
            Log.e(TAG, "onCreate: Service not initialized")
            Toast.makeText(this, "Service not initialized", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        setListener(binder)
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
        val binder = getBinder()!!
        setListener(binder)
        (binder.service as KoishiService).startKoishi()
    }

    fun onStopKoishi(view: View) {
        val service = getBinder()!!.service as KoishiService
        service.stopKoishi()
    }

    override fun onDestroy() {
        super.onDestroy()
        val binder = getBinder() ?: return
        binder.onInput = null
    }

    private fun appendText(text: String) {
        tv.append(text)
        sv.post { sv.fullScroll(View.FOCUS_DOWN) }
    }

    private fun setListener(binder: ProotService.LocalBinder) {
        binder.onInput = {
            runOnUiThread {
                val text = it.replace(Regex("\\e\\[[\\d;]*[^\\d;]"), "")
                appendText("\n$text")
            }
        }
        binder.onExit = {
            runOnUiThread { appendText("\n\n[Process exited.]\n") }
        }
    }
}