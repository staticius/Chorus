package org.chorus_oss.chorus.event.player

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.BookEditPacket

class PlayerEditBookEvent(player: Player, oldBook: Item, newBook: Item, action: BookEditPacket.Action) :
    PlayerEvent(), Cancellable {
    val oldBook: Item
    val action: BookEditPacket.Action

    @JvmField
    var newBook: Item

    init {
        this.player = player
        this.oldBook = oldBook
        this.newBook = newBook
        this.action = action
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
