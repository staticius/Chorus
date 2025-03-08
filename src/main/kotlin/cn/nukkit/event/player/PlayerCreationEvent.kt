package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Event
import cn.nukkit.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
class PlayerCreationEvent(@JvmField var playerClass: Class<out Player>) : Event() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
