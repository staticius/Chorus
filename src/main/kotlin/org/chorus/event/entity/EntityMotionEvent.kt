package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.math.Vector3


open class EntityMotionEvent(entity: Entity?, motion: Vector3) : EntityEvent(), Cancellable {
    val motion: Vector3

    init {
        this.entity = entity
        this.motion = motion
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
