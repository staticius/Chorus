package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList

class PlayerIllegalFlightEvent(player: Player?) : PlayerEvent() {
    var isKick: Boolean = true

    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
