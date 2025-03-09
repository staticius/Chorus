package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.Event

/**
 * @author larryTheCoder (Nukkit Project)
 * @since 7/5/2017
 */
abstract class VehicleEvent(val vehicle: Entity) : Event()
