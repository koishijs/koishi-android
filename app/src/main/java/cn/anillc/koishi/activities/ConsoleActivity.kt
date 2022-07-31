package cn.anillc.koishi.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.showToast

class ConsoleActivity : AppCompatActivity() {

    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binder = (application as KoishiApplication).serviceConnection.koishiBinder
        val service = binder?.service as KoishiService?
        val link = service?.link
        if (binder == null || service!!.process.get() == null || link!!.get() == null) {
            showToast(R.string.koishi_is_not_started)
            finish()
            return
        }

        setContentView(R.layout.console)
        webview = findViewById(R.id.console_webview)
        initWebView()

        webview.loadUrl(link.get())
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView() {
        webview.settings.javaScriptEnabled = true
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
    }
}
