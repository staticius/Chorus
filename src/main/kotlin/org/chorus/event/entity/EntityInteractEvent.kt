package org.chorus.event.entity

import org.chorus.block.Block
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList


class EntityInteractEvent(entity: Entity?, block: Block) : EntityEvent(), Cancellable {
    val block: Block

    init {
        this.entity = entity
        this.block = block
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
