package org.chorus.event.player

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.item.Item

class PlayerGlassBottleFillEvent(player: Player, target: Block, item: Item) :
    PlayerEvent(), Cancellable {
    val item: Item
    val block: Block

    init {
        this.player = player
        this.block = target
        this.item = item.clone()
    }
}
