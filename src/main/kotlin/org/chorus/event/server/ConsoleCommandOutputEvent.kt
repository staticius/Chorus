package org.chorus.event.server

import org.chorus.command.CommandSender
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class ConsoleCommandOutputEvent(val sender: CommandSender, var message: String) : ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
