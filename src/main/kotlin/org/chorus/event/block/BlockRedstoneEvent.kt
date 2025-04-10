package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.HandlerList

class BlockRedstoneEvent(block: Block, val oldPower: Int, val newPower: Int) : BlockEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
