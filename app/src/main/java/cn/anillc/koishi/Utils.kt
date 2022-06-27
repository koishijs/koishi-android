package cn.anillc.koishi

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

fun Process.pid(): Int {
    val clazz = this::class.java
    val pid = clazz.getDeclaredField("pid")
    pid.isAccessible = true
    return pid.get(this) as Int
}