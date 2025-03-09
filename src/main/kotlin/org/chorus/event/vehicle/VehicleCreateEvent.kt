package org.chorus.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList


class VehicleCreateEvent(vehicle: Entity) : VehicleEvent(vehicle), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
