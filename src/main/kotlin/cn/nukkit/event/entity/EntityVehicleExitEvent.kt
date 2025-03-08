package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class EntityVehicleExitEvent(entity: Entity?, vehicle: Entity) : EntityEvent(), Cancellable {
    val vehicle: Entity

    init {
        this.entity = entity
        this.vehicle = vehicle
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
