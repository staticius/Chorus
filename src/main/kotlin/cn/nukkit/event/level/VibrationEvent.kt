package cn.nukkit.event.level

import cn.nukkit.event.Cancellable
import cn.nukkit.event.Event
import cn.nukkit.level.vibration.VibrationEvent

abstract class VibrationEvent(var vibrationEvent: VibrationEvent) : Event(), Cancellable
