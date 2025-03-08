package cn.nukkit.event.block

import cn.nukkit.block.BlockBell
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

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
