package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class BlockHarvestEvent(block: Block, @JvmField var newState: Block, @JvmField var drops: Array<Item>) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
