package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class PlayerToggleSneakEvent(player: Player?, isSneaking: Boolean) : PlayerEvent(), Cancellable {
    val isSneaking: Boolean

    init {
        this.player = player
        this.isSneaking = isSneaking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
