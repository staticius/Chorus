package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * Is called when an vehicle gets destroyed
 */
open class VehicleDestroyEvent
/**
 * Constructor for the VehicleDestroyEvent
 *
 * @param vehicle the destroyed vehicle
 */
    (vehicle: Entity) : VehicleEvent(vehicle), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
