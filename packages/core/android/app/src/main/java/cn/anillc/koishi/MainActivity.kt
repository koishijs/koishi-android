package cn.anillc.koishi

import android.os.Bundle
import com.getcapacitor.BridgeActivity

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(NativeInterface::class.java)
        super.onCreate(savedInstanceState)
    }
}
