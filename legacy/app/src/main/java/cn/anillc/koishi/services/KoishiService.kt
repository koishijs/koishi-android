package cn.anillc.koishi.services

import cn.anillc.koishi.BuildConfig
import cn.anillc.koishi.removeVt100ControlChars
import java.util.concurrent.atomic.AtomicReference

class KoishiService : ProotService() {

    val link = AtomicReference<String>()

    fun startKoishi() {
        startProot("""
            if [ ! -f "yarn.js" ]; then
                echo No yarn.js found.
                exit 1
            fi
            if [ ! -x "yarn.js" ]; then
                chmod +x yarn.js
            fi
            if [ -d "koishi-app-staging" ]; then
                rm -rf koishi-app-staging
            fi
            if [ ! -d "koishi-app" ]; then
                echo Initializing koishi...
                /home/yarn.js create koishi koishi-app --yes --prod
                cd koishi-app
                /home/yarn.js install
            fi
            echo Starting koishi...
            cd /home/koishi-app
            /home/yarn.js koishi start
        """.trimIndent(), mapOf(
            "KOISHI_AGENT" to "Koishi Android/${BuildConfig.VERSION_NAME}",
        ))
    }

    fun stopKoishi() = stopProot()

    private val linkRegex = Regex(".*\\[I] server server listening at (.+)$")
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
