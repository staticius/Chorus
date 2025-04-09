package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.math.Vector3

open class EntityMotionEvent(override var entity: Entity, val motion: Vector3) : EntityEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
