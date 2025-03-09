package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * Called when a player eats something
 */
class PlayerItemConsumeEvent(player: Player?, item: Item) : PlayerEvent(), Cancellable {
    private val item: Item

    init {
        this.player = player
        this.item = item
    }

    fun getItem(): Item {
        return item.clone()
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
