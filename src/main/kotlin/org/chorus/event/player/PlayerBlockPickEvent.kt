package org.chorus.event.player

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item


class PlayerBlockPickEvent(player: Player, val blockClicked: Block, @JvmField var item: Item) :
    PlayerEvent(), Cancellable {
    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
