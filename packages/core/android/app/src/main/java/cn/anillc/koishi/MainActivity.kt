package cn.anillc.koishi

import android.os.Bundle
import com.getcapacitor.BridgeActivity
import java.io.File

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(NativeInterface::class.java)
        super.onCreate(savedInstanceState)

        val files = arrayOf(
            File("$fileDir/home/instances"),
            File("$fileDir/tmp"),
            File("$fileDir/shm")
        )
        for (file in files) {
            if (!(file.exists() || file.mkdirs())) throw Exception("failed to create home")
        }
        if (!File("$fileDir/data/nix").exists()) {
            val data = File("$fileDir/data")
            if (!data.mkdirs()) throw Exception("failed to create data dir")
            Thread {
                val tmp = File("$fileDir/tmp/.data")
                if (tmp.exists()) {
                    if (!tmp.rm()) throw Exception("failed to remove tmp instance")
                }
                if (!tmp.mkdirs()) throw Exception("failed to create tmp instance")
                unpackZip("bootstrap/bootstrap.zip", tmp, this)
                if (!tmp.renameTo(data)) {
                    throw Exception("failed to move tmp to instance")
                }
                KoishiApplication.application.onInitialized()
            }.start()
        } else {
            KoishiApplication.application.onInitialized()
        }
    }
}
