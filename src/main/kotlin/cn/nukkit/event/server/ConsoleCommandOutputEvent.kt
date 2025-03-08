package cn.nukkit.event.server

import cn.nukkit.command.CommandSender
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class ConsoleCommandOutputEvent(val sender: CommandSender, var message: String) : ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
