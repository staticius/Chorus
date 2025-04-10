package org.chorus.event.weather

import org.chorus.entity.weather.EntityLightningStrike
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.level.WeatherEvent
import org.chorus.level.Level

class LightningStrikeEvent(
    level: Level,
    /**
     * Gets the bolt which is striking the earth.
     * @return lightning entity
     */
    val lightning: EntityLightningStrike
) : WeatherEvent(level), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
