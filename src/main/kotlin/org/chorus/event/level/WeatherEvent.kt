package org.chorus.event.level

import org.chorus.event.Event
import org.chorus.level.Level

abstract class WeatherEvent(val level: Level) : Event()
