package org.chorus.console

import org.chorus.Server
import org.chorus.event.server.ServerCommandEvent
import org.chorus.plugin.InternalPlugin
import lombok.RequiredArgsConstructor
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

@RequiredArgsConstructor
class NukkitConsole : SimpleTerminalConsole() {
    private val server: Server? = null
    private val consoleQueue: BlockingQueue<String> = LinkedBlockingQueue()
    private val executingCommands = AtomicBoolean(false)

    override fun isRunning(): Boolean {
        return server!!.isRunning
    }

    override fun runCommand(command: String) {
        if (executingCommands.get()) {
            val event = ServerCommandEvent(server!!.consoleSender, command)
            if (server.pluginManager != null) {
                server.pluginManager.callEvent(event)
            }
            if (!event.isCancelled) {
                Server.getInstance().scheduler.scheduleTask(
                    InternalPlugin.INSTANCE
                ) { server.executeCommand(event.sender, event.command) }
            }
        } else {
            consoleQueue.add(command)
        }
    }

    fun readLine(): String {
        try {
            return consoleQueue.take()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    override fun shutdown() {
        server!!.shutdown()
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader {
        builder.completer(NukkitConsoleCompleter(server))
        builder.appName("PowerNukkitX")
        builder.option(LineReader.Option.HISTORY_BEEP, false)
        builder.option(LineReader.Option.HISTORY_IGNORE_DUPS, true)
        builder.option(LineReader.Option.HISTORY_IGNORE_SPACE, true)
        return super.buildReader(builder)
    }

    fun isExecutingCommands(): Boolean {
        return executingCommands.get()
    }

    fun setExecutingCommands(executingCommands: Boolean) {
        if (this.executingCommands.compareAndSet(!executingCommands, executingCommands) && executingCommands) {
            consoleQueue.clear()
        }
    }
}
