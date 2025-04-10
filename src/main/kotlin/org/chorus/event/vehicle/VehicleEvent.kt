package org.chorus.event.vehicle

import org.chorus.entity.Entity
import org.chorus.event.Event

abstract class VehicleEvent(val vehicle: Entity) : Event()
