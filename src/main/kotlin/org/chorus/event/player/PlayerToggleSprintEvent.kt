package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerToggleSprintEvent(player: Player, isSprinting: Boolean) : PlayerEvent(), Cancellable {
    val isSprinting: Boolean

    init {
        this.player = player
        this.isSprinting = isSprinting
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
