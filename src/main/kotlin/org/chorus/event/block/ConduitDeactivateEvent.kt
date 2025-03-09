package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.HandlerList

class ConduitDeactivateEvent(block: Block) : BlockEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
