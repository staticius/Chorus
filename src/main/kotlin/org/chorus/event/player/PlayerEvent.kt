package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Event


abstract class PlayerEvent : Event() {
    open var player: Player? = null
        protected set
}
