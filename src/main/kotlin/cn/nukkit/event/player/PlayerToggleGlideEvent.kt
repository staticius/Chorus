package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class PlayerToggleGlideEvent(player: Player?, isSneaking: Boolean) : PlayerEvent(), Cancellable {
    val isGliding: Boolean

    init {
        this.player = player
        this.isGliding = isSneaking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
