package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Event

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class PlayerEvent : Event() {
    open var player: Player? = null
        protected set
}
