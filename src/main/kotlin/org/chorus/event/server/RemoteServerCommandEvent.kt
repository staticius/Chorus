package org.chorus.event.server

import org.chorus.command.CommandSender
import org.chorus.event.HandlerList

/**
 * Called when an RCON command is executed.
 *
 * @author Tee7even
 */
class RemoteServerCommandEvent(sender: CommandSender, command: String) :
    ServerCommandEvent(sender, command) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
