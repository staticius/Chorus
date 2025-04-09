package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class PlayerMapInfoRequestEvent(player: Player, item: Item) : PlayerEvent(), Cancellable {
    val map: Item

    init {
        this.player = player
        this.map = item
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
