package org.chorus.event.server

import org.chorus.command.CommandSender
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList


open class ServerCommandEvent(val sender: CommandSender, var command: String) : ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
