package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class EntityVehicleExitEvent(entity: Entity, vehicle: Entity) : EntityEvent(), Cancellable {
    val vehicle: Entity

    init {
        this.entity = entity
        this.vehicle = vehicle
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
