package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList


class VehicleCreateEvent(vehicle: Entity) : VehicleEvent(vehicle), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
