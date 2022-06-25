package cn.anillc.koishi

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class KoishiService : Service() {
    companion object {
        private const val TAG = "KoishiService"
    }

    class LocalBinder(val service: KoishiService) : Binder()

    private val binder = LocalBinder(this)
    override fun onBind(intent: Intent?): IBinder = binder

    private lateinit var process: Process

    fun startKoishi(envPath: String) {
        if (::process.isInitialized) return
        val packagePath = filesDir.path
        val processBuilder = ProcessBuilder(
            "${packagePath}/data/proot-static",
            "-r", "${packagePath}/data${envPath}",
            "-b", "${packagePath}/data/tmp:/tmp",
            "-b", "${packagePath}/data/nix:/nix",
            "-b", "${packagePath}/data:/data",
            "--sysvipc",
            "--link2symlink",
            "/bin/sh", "/bin/login"
        ).redirectErrorStream(true)
        val environment = processBuilder.environment()
        environment["PROOT_TMP_DIR"] = "$packagePath/data/tmp"
        process = processBuilder.start()
        Thread {
            val input = process.inputStream.bufferedReader()
            for (i in input.lines()) {
                Log.i(TAG, "startKoishi: $i")
            }
        }.start()
    }
}