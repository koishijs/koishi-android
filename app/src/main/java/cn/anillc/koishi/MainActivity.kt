package cn.anillc.koishi

import android.os.Bundle
import android.util.Log

class MainActivity : CoroutineActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    override suspend fun onCreateSuspend(savedInstanceState: Bundle?) {
        runOnUiThread { setContentView(R.layout.main) }
        val envPath = install(this)
        Log.i(TAG, "onCreateSuspend: $envPath")
    }
}