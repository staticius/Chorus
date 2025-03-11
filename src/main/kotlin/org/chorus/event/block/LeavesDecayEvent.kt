package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList


class LeavesDecayEvent(block: Block) : BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
