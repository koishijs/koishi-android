package cn.anillc.koishi.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Process

class GoCqhttpService : ProotService() {
    fun startGoCqhttp() {
        startProot("""
            mkdir -p go-cqhttp && cd go-cqhttp
            go-cqhttp -faststart
        """.trimIndent())
    }

    fun stopGoCqhttp() = stopProot()
}