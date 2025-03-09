package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.vibration.VibrationListener

class VibrationArriveEvent(
    vibrationEvent: cn.nukkit.level.vibration.VibrationEvent,
    protected var listener: VibrationListener
) : VibrationEvent(vibrationEvent) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
