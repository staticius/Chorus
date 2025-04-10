package org.chorus.event.vehicle

import org.chorus.entity.Entity

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
