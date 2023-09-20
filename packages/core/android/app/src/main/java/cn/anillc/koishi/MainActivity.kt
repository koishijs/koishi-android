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
        KoishiApplication.application.onInitialized()
//    unpackZip("bootstrap/bootstrap.zip", "data", context)
    }
}
