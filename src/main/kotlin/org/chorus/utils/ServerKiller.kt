package org.chorus.utils

import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class ServerKiller @JvmOverloads constructor(time: Long, unit: TimeUnit = TimeUnit.SECONDS) :
    Thread() {
    val sleepTime: Long = unit.toMillis(time)

    init {
        this.isDaemon = true
        this.name = "Server Killer"
    }

    override fun run() {
        try {
            sleep(sleepTime)
            println("\nTook too long to stop, server was killed forcefully!\n")
            exitProcess(1)
        } catch (e: InterruptedException) {
            // The thread was interrupted, which might mean that Ctrl+C was pressed
            // Support Ctrl+C
            println("\nServer stopping process was interrupted. Killing server...\n")
            exitProcess(1)
        }
    }
}
