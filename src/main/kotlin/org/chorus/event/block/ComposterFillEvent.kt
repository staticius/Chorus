package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class ComposterFillEvent(block: Block, val player: Player, val item: Item, val chance: Int, var isSuccess: Boolean) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
