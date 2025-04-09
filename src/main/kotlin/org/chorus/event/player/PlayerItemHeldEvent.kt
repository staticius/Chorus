package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class PlayerItemHeldEvent(player: Player, item: Item, hotbarSlot: Int) : PlayerEvent(),
    Cancellable {
    val item: Item
    val slot: Int

    init {
        this.player = player
        this.item = item
        this.slot = hotbarSlot
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
