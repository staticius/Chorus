package org.chorus.event.level

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Level

/**
 * @author funcraft (Nukkit Project)
 */
class WeatherChangeEvent(level: Level, private val to: Boolean) : WeatherEvent(level), Cancellable {
    /**
     * Gets the state of weather that the world is being set to
     *
     * @return true if the weather is being set to raining, false otherwise
     */
    fun toWeatherState(): Boolean {
        return to
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
