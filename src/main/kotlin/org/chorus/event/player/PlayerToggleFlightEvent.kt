package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class PlayerToggleFlightEvent(player: Player?, isFlying: Boolean) : PlayerEvent(), Cancellable {
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
