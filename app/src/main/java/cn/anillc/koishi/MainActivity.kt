package cn.anillc.koishi

import android.os.Bundle

class MainActivity : CoroutineActivity() {
    override suspend fun onCreateSuspend(savedInstanceState: Bundle?) {
        runOnUiThread { setContentView(R.layout.main) }
        install(this)
    }
}