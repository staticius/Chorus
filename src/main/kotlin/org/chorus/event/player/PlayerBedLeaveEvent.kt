package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

class PlayerBedLeaveEvent(player: Player?, bed: Block) : PlayerEvent() {
    val bed: Block

    init {
        this.player = player
        this.bed = bed
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
