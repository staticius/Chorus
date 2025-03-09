package org.chorus.event.entity

import org.chorus.block.Block
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * @since 15-10-26
 */
class EntityBlockChangeEvent(entity: Entity?, from: Block, to: Block) :
    EntityEvent(), Cancellable {
    val from: Block
    val to: Block

    init {
        this.entity = entity
        this.from = from
        this.to = to
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
