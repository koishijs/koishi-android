package cn.anillc.koishi.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.core.content.ContextCompat.checkSelfPermission
import android.view.View
import cn.anillc.koishi.*

class MainActivity : AppCompatActivity() {
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
                try {
                    application.envPath = install(this)
                    application.onInitialized()
                    runOnUiThread(::activityMain)
                } catch (e: Exception) {
                    runOnUiThread {
                        showToast(R.string.failed_to_initialize, e.toString())
                        finish()
                    }
                }
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
