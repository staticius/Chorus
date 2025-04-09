package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerToggleFlightEvent(player: Player, isFlying: Boolean) : PlayerEvent(), Cancellable {
    @JvmField
    val isFlying: Boolean

    init {
        this.player = player
        this.isFlying = isFlying
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
