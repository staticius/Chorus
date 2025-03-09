package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.HandlerList
import org.chorus.level.Transform

class VehicleMoveEvent(vehicle: Entity, val from: Transform, val to: Transform) :
    VehicleEvent(vehicle) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
