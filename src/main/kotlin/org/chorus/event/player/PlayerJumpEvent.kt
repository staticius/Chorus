package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.HandlerList

class PlayerJumpEvent(player: Player) : PlayerEvent() {
    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
