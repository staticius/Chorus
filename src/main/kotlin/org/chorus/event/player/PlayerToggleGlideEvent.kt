package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerToggleGlideEvent(player: Player, isSneaking: Boolean) : PlayerEvent(), Cancellable {
    val isGliding: Boolean

    init {
        this.player = player
        this.isGliding = isSneaking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
