package cn.anillc.koishi.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import android.widget.Toast
import cn.anillc.koishi.*
import java.io.File
import java.io.FileOutputStream


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
                val input = contentResolver.openInputStream(data!!.data!!)
                input.use { FileOutputStream(koishiZip).use(it::copyTo) }
            } catch (e: Exception) {
                showToast(R.string.import_file_failed)
                Log.e(TAG, "onActivityResult: failed to import file", e)
                return@Thread
            }

            val zipList = startProotProcessWait(
                "unzip -l koishi.zip | grep koishi.yml",
                packagePath, koishiApplication.envPath
            )

            if (zipList == null || !zipList.contains("koishi.yml")) {
                showToast(R.string.invalid_file)
                runOnUiThread(dismiss)
                koishiZip.delete()
                return@Thread
            }

            try {
                val oldKoishi = File("${filesDir}/home/koishi-app")
                if (oldKoishi.exists()) deleteFolder(oldKoishi)
            } catch (e: Exception) {
                showToast(R.string.failed_to_delete_koishi)
                Log.e(TAG, "onActivityResult: failed to delete koishi-app", e)
                return@Thread
            }

            showToast(R.string.import_file_succeed)
            runOnUiThread(dismiss)
        }.start()
    }

    private fun showToast(resId: Int) = runOnUiThread {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }
}