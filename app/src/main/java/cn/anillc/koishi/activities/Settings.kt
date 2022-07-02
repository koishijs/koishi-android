package cn.anillc.koishi.activities

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import cn.anillc.koishi.BuildConfig
import cn.anillc.koishi.R

class Settings : FragmentActivity() {
    class KoishiPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings, rootKey)
            findPreference("about").summary = "Koishi Android v${BuildConfig.VERSION_NAME}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceFragment = KoishiPreferenceFragment()
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, preferenceFragment)
            .commit()
    }
}