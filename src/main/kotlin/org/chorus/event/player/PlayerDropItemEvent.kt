package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class PlayerDropItemEvent(player: Player, drop: Item) : PlayerEvent(), Cancellable {
    val item: Item

    init {
        this.player = player
        this.item = drop
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
