package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author CreeperFace
 * @since 18.3.2017
 */
class PlayerMapInfoRequestEvent(p: Player?, item: Item) : PlayerEvent(), Cancellable {
    val map: Item

    init {
        this.player = p
        this.map = item
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
