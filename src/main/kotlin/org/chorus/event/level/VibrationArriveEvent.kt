package org.chorus.event.level

import cn.nukkit.event.HandlerList
import cn.nukkit.level.vibration.VibrationListener

class VibrationArriveEvent(
    vibrationEvent: cn.nukkit.level.vibration.VibrationEvent,
    protected var listener: VibrationListener
) : VibrationEvent(vibrationEvent) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
