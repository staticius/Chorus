package org.chorus.event.block

import org.chorus.block.BlockBell
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class BellRingEvent(bell: BlockBell, val cause: RingCause, val entity: Entity) : BlockEvent(bell), Cancellable {
    override fun getBlock(): BlockBell? {
        return super.getBlock() as BlockBell
    }

    enum class RingCause {
        HUMAN_INTERACTION,
        REDSTONE,
        PROJECTILE,
        DROPPED_ITEM,
        UNKNOWN
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
