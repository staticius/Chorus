package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.Level
import org.chorus.level.Locator


class SpawnChangeEvent(level: Level, val previousSpawn: Locator) : LevelEvent(level) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
