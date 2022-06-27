package cn.anillc.koishi.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Process

class KoishiService : ProotService() {
    fun startKoishi() {
        startProot("""
            echo Starting koishi...
            yarn start
        """.trimIndent())
    }

    fun stopKoishi() = stopProot()
}