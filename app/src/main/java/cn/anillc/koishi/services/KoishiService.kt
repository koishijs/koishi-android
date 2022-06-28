package cn.anillc.koishi.services

import cn.anillc.koishi.removeVt100ControlChars
import java.util.concurrent.atomic.AtomicReference

class KoishiService : ProotService() {

    val link = AtomicReference<String>()

    fun startKoishi() {
        startProot("""
            echo Starting koishi...
            cd koishi-app
            yarn start
        """.trimIndent(), mapOf("KOI" to "KOI"))
    }

    fun stopKoishi() = stopProot()

    private val linkRegex = Regex(".*\\[I] app server listening at (.+)$")
    override fun onInput(line: String) {
        if (link.get() == null) {
            val match = linkRegex.matchEntire(line.removeVt100ControlChars())
            if (match != null) link.set(match.groupValues[1])
        }
    }

    override fun onExit(code: Int) {
        link.set(null)
    }
}