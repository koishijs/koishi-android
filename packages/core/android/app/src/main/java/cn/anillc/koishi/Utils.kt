package cn.anillc.koishi

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val fileDir by lazy {
    
}

fun startProotProcess(
    cmd: String,
    packagePath: String,
    envPath: String,
    env: Map<String, String> = mapOf(),
): Process {
    val processBuilder = ProcessBuilder(
        "$packagePath/data/proot-static",
        "-r", "$packagePath/data${envPath}",
        "-b", "$packagePath/tmp:/tmp",
        "-b", "$packagePath/shm:/dev/shm",
        "-b", "$packagePath/data/nix:/nix",
        "-b", "$packagePath/data:/data",
        "-b", "$packagePath/home:/home",
        "-b", "/proc:/proc",
        "-b", "/dev:/dev",
        "--sysvipc",
        "--link2symlink",
        "/bin/sh", "/bin/login", "-c", cmd
    ).redirectErrorStream(true)
    val environment = processBuilder.environment()
    environment.putAll(env)
    environment["PROOT_TMP_DIR"] = "$packagePath/tmp"
    return processBuilder.start()
}

fun startProotProcessWait(
    cmd: String,
    packagePath: String,
    envPath: String,
    env: Map<String, String> = mapOf(),
): String? {
    val process = startProotProcess(cmd, packagePath, envPath, env)
    if (process.waitFor() != 0) return null
    return process.inputStream.bufferedReader().use(BufferedReader::readText)
}

//fun acceptAlert(context: Context, message: Int, callback: DialogInterface.OnClickListener) =
//    AlertDialog.Builder(context)
//        .setMessage(message)
//        .setPositiveButton(android.R.string.ok, callback)
//        .setNegativeButton(android.R.string.cancel) { _, _ -> }
//        .create().show()

//@SuppressLint("InflateParams")
//fun loadingAlert(context: Context, message: Int): () -> Unit {
//    val layout = LayoutInflater.from(context).inflate(R.layout.loading_alert, null)
//    layout.findViewById<TextView>(R.id.loading_alert_text).setText(message)
//    val dialog = AlertDialog.Builder(context)
//        .setCancelable(false)
//        .setView(layout)
//        .create()
//    dialog.show()
//    return dialog::dismiss
//}

fun Process.pid(): Int {
    val clazz = this::class.java
    val pid = clazz.getDeclaredField("pid")
    pid.isAccessible = true
    return pid.get(this) as Int
}

fun String.removeVt100ControlChars(): String =
    replace(Regex("\\e\\[[\\d;]*[^\\d;]"), "")

fun condition(): Pair<Lock, Condition> {
    val lock = ReentrantLock()
    return lock to lock.newCondition()
}

fun Pair<Lock, Condition>.wait() = first.withLock(second::await)

fun Pair<Lock, Condition>.wait(times: Long, unit: TimeUnit) =
    first.withLock { second.await(times, unit) }

fun Pair<Lock, Condition>.notifyAll() = first.withLock(second::signalAll)

// File.delete fails on EMUI
fun File.rm(): Boolean {
    return Runtime.getRuntime().exec("rm -rf $absolutePath").waitFor() == 0
}

fun AppCompatActivity.showToastOnUiThread(text: String) = runOnUiThread {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.showToastOnUiThread(resId: Int, vararg args: String) = runOnUiThread {
    Toast.makeText(this, getString(resId, args), Toast.LENGTH_LONG).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.showToast(resId: Int, vararg args: String) {
    Toast.makeText(this, getString(resId, args), Toast.LENGTH_LONG).show()
}
