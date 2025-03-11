package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.Level


class LevelLoadEvent(level: Level) : LevelEvent(level) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
