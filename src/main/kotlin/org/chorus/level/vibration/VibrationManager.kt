package org.chorus.level.vibration


interface VibrationManager {
    fun callVibrationEvent(event: VibrationEvent)

    fun addListener(listener: VibrationListener)

    fun removeListener(listener: VibrationListener)
}
