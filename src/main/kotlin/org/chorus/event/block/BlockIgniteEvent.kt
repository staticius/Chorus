package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class BlockIgniteEvent(block: Block, val source: Block?, val entity: Entity?, val cause: BlockIgniteCause) :
    BlockEvent(block), Cancellable {
    enum class BlockIgniteCause {
        EXPLOSION,
        FIREBALL,
        FLINT_AND_STEEL,
        LAVA,
        LIGHTNING,
        SPREAD
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
