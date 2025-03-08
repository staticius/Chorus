package cn.nukkit.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityVehicle
import cn.nukkit.event.HandlerList

/**
 * Is called when an entity damages a vehicle
 *
 * @author TrainmasterHD
 * @since 09.09.2019
 */
class VehicleDamageByEntityEvent
/**
 * Constructor for the VehicleDamageByEntityEvent
 *
 * @param vehicle  the damaged vehicle
 * @param attacker the attacking vehicle
 * @param damage   the caused damage on the vehicle
 */(
    vehicle: EntityVehicle,
    /**
     * Returns the attacking entity
     *
     * @return attacking entity
     */
    val attacker: Entity, damage: Double
) :
    VehicleDamageEvent(vehicle, damage) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
