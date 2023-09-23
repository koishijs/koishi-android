package cn.anillc.koishi

import android.content.Context
import androidx.preference.PreferenceManager
import java.io.File
import java.io.FileOutputStream

val script = """
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
        if [ ! -f "koishi.zip" ]; then
            echo Failed to extract koishi.
            exit 1
        fi
        echo Initializing koishi...
        unzip -d koishi-app-staging koishi.zip > /dev/null 2>&1
        mv koishi-app-staging koishi-app
        rm -f koishi.zip
    fi
    echo Starting koishi...
    cd koishi-app
    /home/yarn.js koishi start
""".trimIndent()

val linkRegex = Regex(".*\\[I] app server listening at (.+)$")


class Instance(
    val name: String,
    private val context: Context,
) {
    private val instance = File("$fileDir/home/instances/$name")
    private var proot: Proot? = null
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    var link: String? = null

    init {
        init()
    }

    private fun init() {
        if (instance.exists()) return
        val copyFile = { src: String, dst: String ->
            context.assets.open(src).use {
                FileOutputStream(dst).use(it::copyTo)
            }
        }
        val tmp = File("$fileDir/tmp/.instance")
        if (tmp.exists()) {
            if (!tmp.rm()) throw Exception("failed to remove tmp instance")
        }
        if (!tmp.mkdirs()) throw Exception("failed to create tmp instance")
        copyFile("bootstrap/yarn.js", "${tmp.path}/yarn.js")
        unpackZip("bootstrap/koishi.zip", tmp, context)
        if (!tmp.renameTo(instance)) {
            throw Exception("failed to move tmp to instance")
        }
    }

    fun start() {
        val proot = this.proot
        if (proot?.status() is Proot.Status.Starting || proot?.status() is Proot.Status.Running) {
            return
        }
        val env = mapOf(
            "KOISHI_AGENT" to "Koishi Android/${BuildConfig.VERSION_NAME}",
            "KOISHI_DNS" to preferences.getString("KOISHI_DNS", DEFAULT_DNS)!!,
            "KOISHI_TIMEZONE" to preferences.getString("KOISHI_TIMEZONE", DEFAULT_TIMEZONE)!!,
        )
        this.proot = object : Proot(script, instance.path, envPath, env) {
            override fun onData(line: String) {
                if (link == null) {
                    val match = linkRegex.matchEntire(line.removeVt100ControlChars())
                    if (match != null) link = match.groupValues[1]
                }
            }

            override fun onExit(status: Int?) {
                link = null
            }
        }
    }

    fun stop() {
        this.proot?.stop()
        this.proot = null
    }

    fun status(): Proot.Status? {
        return this.proot?.status()
    }
}
