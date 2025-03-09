package org.chorus.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

class BlockHarvestEvent(block: Block, @JvmField var newState: Block, @JvmField var drops: Array<Item>) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
