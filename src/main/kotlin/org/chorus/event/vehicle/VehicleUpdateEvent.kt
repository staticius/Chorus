package org.chorus.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.event.HandlerList

class VehicleUpdateEvent(vehicle: Entity) : VehicleEvent(vehicle) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
