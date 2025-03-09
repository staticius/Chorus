package org.chorus.event.redstone

import org.chorus.block.Block
import org.chorus.event.HandlerList
import org.chorus.event.block.BlockUpdateEvent

/**
 * @author Angelic47 (Nukkit Project)
 */
class RedstoneUpdateEvent(source: Block) : BlockUpdateEvent(source) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}

