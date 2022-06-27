package cn.anillc.koishi.services

class KoishiService : ProotService() {
    fun startKoishi() {
        startProot("""
            echo Starting koishi...
            cd koishi-app
            yarn start
        """.trimIndent(), mapOf("KOI" to "KOI"))
    }

    fun stopKoishi() = stopProot()
}