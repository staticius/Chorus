package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.Level

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LevelSaveEvent(level: Level) : LevelEvent(level) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
