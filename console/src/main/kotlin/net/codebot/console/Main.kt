package net.codebot.console

import net.codebot.shared.SysInfo

fun main() {
    println("Console Application:")
    println("Hello ${SysInfo.userName}")

    val runtime = Runtime.getRuntime()
    val process = runtime.exec("cd ../../../resources && ./application")
    println(process.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() })
}