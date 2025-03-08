package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList

class PlayerJumpEvent(player: Player?) : PlayerEvent() {
    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
