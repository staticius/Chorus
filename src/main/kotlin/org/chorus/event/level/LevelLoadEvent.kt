package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.Level

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LevelLoadEvent(level: Level) : LevelEvent(level) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
