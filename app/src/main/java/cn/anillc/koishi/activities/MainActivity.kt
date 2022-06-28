package cn.anillc.koishi.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.install
import cn.anillc.koishi.services.KoishiService

class MainActivity : Activity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var koishiApplication: KoishiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        koishiApplication = application as KoishiApplication

        val application = koishiApplication
        if (!application.isEnvPathInitialized) {
            setContentView(R.layout.loading)
            Thread {
                application.envPath = install(this)
                application.onInitialized()
                runOnUiThread(::activityMain)
            }.start()
        } else {
            activityMain()
        }
    }

    private fun activityMain() {
        setContentView(R.layout.main)
    }

    fun onManageKoishi(view: View) =
        startActivity(Intent(this, KoishiActivity::class.java))

    fun onStartWebView(view: View) =
        startActivity(Intent(this, ConsoleActivity::class.java))

}