package org.chorus.event.level

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Level

/**
 * @author funcraft (Nukkit Project)
 */
class ThunderChangeEvent(level: Level, private val to: Boolean) : WeatherEvent(level), Cancellable {
    /**
     * Gets the state of thunder that the world is being set to
     *
     * @return true if the thunder is being set to start, false otherwise
     */
    fun toThunderState(): Boolean {
        return to
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
