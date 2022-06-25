package cn.anillc.koishi

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import cn.anillc.koishi.base.CoroutineService
import kotlinx.coroutines.withContext
import org.apache.commons.exec.CommandLine

class KoishiService : CoroutineService() {
    companion object {
        private const val TAG = "KoishiService"
    }

    class LocalBinder(val service: KoishiService) : Binder()
    private val binder = LocalBinder(this)
    override fun onBind(intent: Intent?): IBinder = binder

    private lateinit var process: Process

    suspend fun startKoishi(envPath: String) {
        if (::process.isInitialized) return
        withContext(coroutineContext) {
            vertx.execute {
                val packagePath = filesDir.path
                val processBuilder = ProcessBuilder(
                    "${packagePath}/data/proot-static",
                    "-r", "${packagePath}/data",
                    "-b", "${packagePath}/data/tmp:/tmp",
                    "-b", "${packagePath}/data${envPath}/bin:/bin",
                    "-b", "${packagePath}/data${envPath}/etc:/etc",
                    "-b", "${packagePath}/data${envPath}/lib:/lib",
                    "-b", "${packagePath}/data${envPath}/share:/share",
                    "--link2symlink",
                    "/bin/sh", "-c", "/bin/login"
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
    }
}