package org.chorus.event.vehicle

import cn.nukkit.entity.Entity
import cn.nukkit.event.Event

/**
 * @author larryTheCoder (Nukkit Project)
 * @since 7/5/2017
 */
abstract class VehicleEvent(val vehicle: Entity) : Event()
