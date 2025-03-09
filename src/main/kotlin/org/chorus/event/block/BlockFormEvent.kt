package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class BlockFormEvent(block: Block, newState: Block?) :
    BlockGrowEvent(block, newState) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
