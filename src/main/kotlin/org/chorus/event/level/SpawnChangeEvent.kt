package org.chorus.event.level

import cn.nukkit.event.HandlerList
import cn.nukkit.level.Level
import cn.nukkit.level.Locator

/**
 * @author MagicDroidX (Nukkit Project)
 */
class SpawnChangeEvent(level: Level, val previousSpawn: Locator) : LevelEvent(level) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
