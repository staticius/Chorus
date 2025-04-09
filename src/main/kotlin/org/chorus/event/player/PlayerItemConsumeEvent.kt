package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class PlayerItemConsumeEvent(player: Player, item: Item) : PlayerEvent(), Cancellable {
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
