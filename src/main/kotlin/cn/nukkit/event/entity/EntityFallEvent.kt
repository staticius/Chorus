package cn.nukkit.event.entity

import cn.nukkit.block.Block
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class EntityFallEvent(entity: Entity?, blockFallOn: Block, fallDistance: Float) :
    EntityEvent(), Cancellable {
    val blockFallOn: Block
    var fallDistance: Float

    init {
        this.entity = entity
        this.blockFallOn = blockFallOn
        this.fallDistance = fallDistance
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
