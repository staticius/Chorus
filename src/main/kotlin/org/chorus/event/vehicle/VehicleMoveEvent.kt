package org.chorus.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Transform

class VehicleMoveEvent(vehicle: Entity, val from: Transform, val to: Transform) :
    VehicleEvent(vehicle) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
