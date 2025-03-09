package org.chorus.network.rcon

import cn.nukkit.Server
import cn.nukkit.command.RemoteConsoleCommandSender
import cn.nukkit.event.server.RemoteServerCommandEvent
import cn.nukkit.utils.TextFormat
import lombok.extern.slf4j.Slf4j
import java.io.IOException

/**
 * Implementation of Source RCON protocol.
 * https://developer.valvesoftware.com/wiki/Source_RCON_Protocol
 *
 *
 * Wrapper for RCONServer. Handles data.
 *
 * @author Tee7even
 */
@Slf4j
class RCON(server: Server, password: String, address: String, port: Int) {
    private val server: Server
    private var serverThread: RCONServer? = null

    init {
        require(!password.isEmpty()) { "nukkit.server.rcon.emptyPasswordError" }

        this.server = server

        try {
            this.serverThread = RCONServer(address, port, password)
            serverThread.start()
        } catch (e: IOException) {
            throw IllegalArgumentException("nukkit.server.rcon.startupError", e)
        }

        RCON.log.info(this.server.language.tr("nukkit.server.rcon.running", *arrayOf<String>(address, port.toString())))
    }

    fun check() {
        if (this.serverThread == null) {
            return
        } else if (!serverThread.isAlive) {
            return
        }

        var command: RCONCommand
        while ((serverThread.receive().also { command = it!! }) != null) {
            val sender = RemoteConsoleCommandSender()
            val event = RemoteServerCommandEvent(sender, command.command)
            server.pluginManager.callEvent(event)

            if (!event.isCancelled) {
                server.executeCommand(sender, command.command)
            }

            serverThread.respond(command.sender, command.id, TextFormat.clean(sender.getMessages()))
        }
    }

    fun close() {
        try {
            synchronized(serverThread!!) {
                serverThread.close()
                (serverThread as Object).wait(5000)
            }
        } catch (exception: InterruptedException) {
            //
        }
    }
}
