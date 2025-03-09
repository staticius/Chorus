package org.chorus.event.level

import cn.nukkit.event.HandlerList

class VibrationOccurEvent(vibrationEvent: cn.nukkit.level.vibration.VibrationEvent) : VibrationEvent(vibrationEvent) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
