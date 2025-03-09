package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.HandlerList

class VehicleUpdateEvent(vehicle: Entity) : VehicleEvent(vehicle) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
