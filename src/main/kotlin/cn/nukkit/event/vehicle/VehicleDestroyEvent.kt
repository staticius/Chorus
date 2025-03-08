package cn.nukkit.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

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
