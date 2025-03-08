package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

class PlayerDropItemEvent(player: Player?, drop: Item) : PlayerEvent(), Cancellable {
    val item: Item

    init {
        this.player = player
        this.item = drop
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
