package org.chorus.event.command

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.block.BlockEvent

class CommandBlockExecuteEvent(block: Block, var command: String) : BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
