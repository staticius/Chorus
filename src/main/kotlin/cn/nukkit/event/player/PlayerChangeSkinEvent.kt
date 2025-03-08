package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.entity.data.Skin
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @author KCodeYT (Nukkit Project)
 */
class PlayerChangeSkinEvent(player: Player?, skin: Skin) : PlayerEvent(), Cancellable {
    val skin: Skin

    init {
        this.player = player
        this.skin = skin
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
