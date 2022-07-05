package cn.anillc.koishi.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.services.KoishiService
import cn.anillc.koishi.services.ProotService
import cn.anillc.koishi.showToast

class ConsoleActivity : Activity() {

    private lateinit var webview: WebView

    private val webViewClient = object : WebViewClient() {
        override fun onLoadResource(view: WebView, url: String) {
            view.evaluateJavascript(
                "document.querySelector('meta[name=\"viewport\"]').setAttribute('content', 'width=1920');",
                null
            )
        }
    }

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
        webview.settings.builtInZoomControls = true
        webview.settings.setSupportZoom(true)
        webview.webViewClient = webViewClient
    }
}