package org.chorus.event.level

import org.chorus.event.Cancellable
import org.chorus.event.Event
import org.chorus.level.vibration.VibrationEvent

abstract class VibrationEvent(var vibrationEvent: VibrationEvent) : Event(), Cancellable
