package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerCommandPreprocessEvent(player: Player?, message: String?) : PlayerMessageEvent(), Cancellable {
    init {
        this.player = player
        this.message = message
    }

    override var player: Player?
        get() = super.player
        set(player) {
            this.player = player
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
