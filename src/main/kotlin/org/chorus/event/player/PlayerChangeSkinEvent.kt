package org.chorus.event.player

import org.chorus.Player
import org.chorus.entity.data.Skin
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerChangeSkinEvent(player: Player, skin: Skin) : PlayerEvent(), Cancellable {
    val skin: Skin

    init {
        this.player = player
        this.skin = skin
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
