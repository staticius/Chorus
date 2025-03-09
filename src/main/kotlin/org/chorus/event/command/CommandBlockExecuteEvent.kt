package org.chorus.event.command

import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.block.BlockEvent

class CommandBlockExecuteEvent(block: Block, var command: String) : BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
