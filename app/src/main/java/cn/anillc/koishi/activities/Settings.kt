package cn.anillc.koishi.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import cn.anillc.koishi.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class Settings : FragmentActivity(), Preference.OnPreferenceClickListener {

    companion object {
        val TAG = this::class.simpleName
    }

    class KoishiPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings, rootKey)
            findPreference("about").summary = ABOUT
        }
    }

    private lateinit var koishiApplication: KoishiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koishiApplication = application as KoishiApplication

        val preferenceFragment = KoishiPreferenceFragment()
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, preferenceFragment)
            .commitNow()
        preferenceFragment.findPreference("export").onPreferenceClickListener = this
        preferenceFragment.findPreference("import").onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "export" -> exportKoishi()
            "import" -> importKoishi()
            else -> throw Exception("Unknown preference")
        }
        return true
    }

    private fun exportKoishi() {
        val read = checkSelfPermission(this, READ_EXTERNAL_STORAGE)
        val write = checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        if (read != PERMISSION_GRANTED || write != PERMISSION_GRANTED) {
            showToast(R.string.permission_denied)
            return
        }
        val packagePath = filesDir.path
        val dismiss = loadingAlert(this, R.string.export_loading)
        val koishiApp = File("$packagePath/home/koishi-app")
        val koishiExportZip = File("$packagePath/home/koishi-export.zip")
        val external = File(Environment.getExternalStorageDirectory(), "koishi")
        Thread {
            try {
                if (!koishiApp.exists()) {
                    showToastOnUiThread(R.string.koishi_not_initialized)
                    return@Thread
                }

                val exitValue = startProotProcess(
                    """
                        cd koishi-app
                        zip -9qry ../koishi-export.zip ./.
                    """.trimIndent(), packagePath, koishiApplication.envPath
                ).waitFor()

                if (exitValue != 0) throw Exception()
                if (!external.exists() && !external.mkdirs()) throw Exception()

                val today = Date()
                val date = DateFormat.format("yyyy-MM-dd", today)
                val backups = external.list()
                val backupRegex = Regex("^koishi-$date-(\\d+)\\.zip\$")
                val last = backups.mapNotNull(backupRegex::matchEntire)
                    .map { it.groupValues[1].toInt() }.sorted().reversed().getOrNull(0)
                val num = (if (last == null) 1 else last + 1)
                    .toString().padStart(2, '0')
                val saveFile = File(external, "koishi-$date-$num.zip")
                FileInputStream(koishiExportZip).use {
                    FileOutputStream(saveFile).use(it::copyTo)
                }
                koishiExportZip.rm()
                showToastOnUiThread(getString(R.string.export_file_succeed, saveFile.absolutePath))
            } catch (e: Exception) {
                showToastOnUiThread(R.string.export_file_failed)
                Log.e(TAG, "exportKoishi: failed to export koishi", e)
            } finally {
                runOnUiThread(dismiss)
            }
        }.start()
    }

    private val pickerCode = 514

    private fun importKoishi() = acceptAlert(this, R.string.import_koishi_alert) { _, _ ->
        startActivityForResult(
            Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/zip"
            }, "Choose a file"),
            pickerCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != pickerCode || resultCode != Activity.RESULT_OK) return

        val packagePath = filesDir.path
        val dismiss = loadingAlert(this, R.string.import_loading)
        val koishiZip = File("$packagePath/home/koishi.zip")
        Thread {
            try {
                try {
                    val input = contentResolver.openInputStream(data!!.data!!)
                    input.use { FileOutputStream(koishiZip).use(it::copyTo) }
                } catch (e: Exception) {
                    showToastOnUiThread(R.string.import_file_failed)
                    throw e
                }

                val zipList = startProotProcessWait(
                    "unzip -l koishi.zip | grep koishi.yml",
                    packagePath, koishiApplication.envPath
                )

                if (zipList == null || !zipList.contains("koishi.yml")) {
                    showToastOnUiThread(R.string.invalid_file)
                    koishiZip.rm()
                    return@Thread
                }

                try {
                    val oldKoishi = File("${packagePath}/home/koishi-app")
                    if (oldKoishi.exists() && !oldKoishi.rm()) {
                        throw Exception("failed to delete koishi")
                    }
                } catch (e: Exception) {
                    showToastOnUiThread(R.string.failed_to_delete_koishi)
                    throw e
                }

                showToastOnUiThread(R.string.import_file_succeed)
            } catch (e: Exception) {
                Log.e(TAG, "onActivityResult: failed to import koishi", e)
            } finally {
                runOnUiThread(dismiss)
            }
        }.start()
    }
}