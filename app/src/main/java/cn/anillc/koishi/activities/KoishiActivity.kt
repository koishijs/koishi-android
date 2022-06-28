package cn.anillc.koishi.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.removeVt100ControlChars
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.services.ProotService

class KoishiActivity : Activity() {

    companion object {
        val TAG = this::class.simpleName
    }

    private lateinit var koishiApplication: KoishiApplication
    private lateinit var tv: TextView
    private lateinit var sv: ScrollView
    private lateinit var binder: ProotService.LocalBinder
    private lateinit var koishiService: KoishiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koishiApplication = application as KoishiApplication
        setContentView(R.layout.koishi)
        tv = findViewById(R.id.go_cqhttp_tv)
        sv = tv.parent as ScrollView

        val binder = getBinder() ?: return
        this.binder = binder
        this.koishiService = binder.service as KoishiService

        appendText(koishiService.log.get())
        setListener()
    }

    private fun getBinder(): ProotService.LocalBinder? {
        val binder = koishiApplication.serviceConnection.koishiBinder
        if (binder == null) {
            Toast.makeText(this, R.string.failed_to_get_service, Toast.LENGTH_LONG).show()
            finish()
            return null
        }
        return binder
    }

    fun onStartKoishi(view: View) {
        setListener()
        koishiService.startKoishi()
    }


    fun onStopKoishi(view: View) =
        koishiService.stopKoishi()

    override fun onDestroy() {
        super.onDestroy()
        removeListener()
    }

    private fun appendText(text: String) {
        tv.append(text)
        sv.post { sv.fullScroll(View.FOCUS_DOWN) }
    }

    private fun setListener() {
        binder.onInput = {
            runOnUiThread {
                val text = it.removeVt100ControlChars()
                appendText("\n$text")
            }
        }
    }

    private fun removeListener() {
        binder.onInput = null
        binder.onExit = null
    }
}