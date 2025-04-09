package org.chorus.event.player

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.HandlerList

class PlayerBedLeaveEvent(player: Player, bed: Block) : PlayerEvent() {
    val bed: Block

    init {
        this.player = player
        this.bed = bed
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
