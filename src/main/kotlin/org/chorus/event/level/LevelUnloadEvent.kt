package org.chorus.event.level

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Level


class LevelUnloadEvent(level: Level) : LevelEvent(level), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
