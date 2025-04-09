package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerToggleSneakEvent(player: Player, isSneaking: Boolean) : PlayerEvent(), Cancellable {
    val isSneaking: Boolean

    init {
        this.player = player
        this.isSneaking = isSneaking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
