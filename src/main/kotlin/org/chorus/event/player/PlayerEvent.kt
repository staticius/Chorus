package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Event

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class PlayerEvent : Event() {
    open var player: Player? = null
        protected set
}
