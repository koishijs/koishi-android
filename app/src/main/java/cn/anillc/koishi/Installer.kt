package cn.anillc.koishi

import android.app.ProgressDialog
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

suspend fun install(activity: CoroutineActivity) {
    val packageData = activity.filesDir.path
    val dataPath = "$packageData/data"

    if (activity.vertx.fileSystem().exists(dataPath).await()) return

    activity.vertx.execute {
        var from: InputStream? = null
        var to: OutputStream? = null
        try {
            from = activity.assets.open("bootstrap/env.txt")
            to = FileOutputStream("$packageData/env.txt")
            from.copyTo(to)
        } finally {
            from?.close()
            to?.close()
        }
    }.await()

    activity.runOnUiThread {
        ProgressDialog.show(activity, "test", "www")
    }

}