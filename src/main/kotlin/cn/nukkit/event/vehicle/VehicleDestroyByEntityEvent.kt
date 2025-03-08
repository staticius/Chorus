package cn.nukkit.event.vehicle

import cn.nukkit.entity.Entity

/**
 * Is called when an entity destroyed a vehicle
 *
 * @author TrainmasterHD
 * @since 09.09.2019
 */
class VehicleDestroyByEntityEvent
/**
 * Constructor for the VehicleDestroyByEntityEvent
 *
 * @param vehicle   the destroyed vehicle
 * @param destroyer the destroying entity
 */(
    vehicle: Entity,
    /**
     * Returns the destroying entity
     *
     * @return destroying entity
     */
    val destroyer: Entity
) :
    VehicleDestroyEvent(vehicle)
