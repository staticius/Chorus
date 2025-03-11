package org.chorus.event.level

import org.chorus.event.HandlerList

class VibrationOccurEvent(vibrationEvent: org.chorus.level.vibration.VibrationEvent) : VibrationEvent(vibrationEvent) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
