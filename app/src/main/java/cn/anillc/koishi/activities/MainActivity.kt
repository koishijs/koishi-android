package cn.anillc.koishi.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.install

class MainActivity : Activity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = application as KoishiApplication
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

    fun onManageGoCqhttp(view: View) =
        startActivity(Intent(this, GoCqhttpActivity::class.java))

    fun onManageKoishi(view: View) =
        startActivity(Intent(this, KoishiActivity::class.java))

    fun onStartWebView(view: View) {

    }

}