package cn.anillc.koishi.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.View
import cn.anillc.koishi.KoishiApplication
import cn.anillc.koishi.R
import cn.anillc.koishi.acceptAlert
import cn.anillc.koishi.install

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
                // TODO: exception
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
        requestPermission()
    }

    fun onManageKoishiClick(view: View) =
        startActivity(Intent(this, KoishiActivity::class.java))

    fun onStartWebViewClick(view: View) =
        startActivity(Intent(this, ConsoleActivity::class.java))

    fun onSettingsClick(view: View) =
        startActivity(Intent(this, Settings::class.java))

    private fun requestPermission() {
        val read = checkSelfPermission(this, READ_EXTERNAL_STORAGE)
        val write = checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        if (read == PERMISSION_GRANTED && write == PERMISSION_GRANTED) return
        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), 114)
    }

}