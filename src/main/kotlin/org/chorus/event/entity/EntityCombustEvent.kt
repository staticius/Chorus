package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

open class EntityCombustEvent(override var entity: Entity, @JvmField var duration: Int) : EntityEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
