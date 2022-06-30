package cn.anillc.koishi.services

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
            if [ ! -d "koishi-app" ]; then
                if [ ! -f "koishi.zip" ]; then
                    echo Failed to extract koishi.
                    exit 1
                fi
                echo Initializing koishi...
                unzip -d koishi-app koishi.zip > /dev/null 2>&1
                rm -f koishi.zip
            fi
            echo Starting koishi...
            cd koishi-app
            # /home/yarn.js start
            # yarn start
            ./node_modules/.bin/koishi start
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