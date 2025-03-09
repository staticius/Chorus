package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

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
