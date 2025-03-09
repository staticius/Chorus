package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
class BlockSpreadEvent(block: Block, val source: Block, newState: Block?) :
    BlockFormEvent(block, newState) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
