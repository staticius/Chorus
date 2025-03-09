package org.chorus.event.vehicle

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class EntityEnterVehicleEvent(val entity: Entity, vehicle: Entity) : VehicleEvent(vehicle),
    Cancellable {
    val isPlayer: Boolean
        get() = entity is Player

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
