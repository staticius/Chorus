package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.HandlerList

class PlayerIllegalFlightEvent(player: Player) : PlayerEvent() {
    var isKick: Boolean = true

    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
