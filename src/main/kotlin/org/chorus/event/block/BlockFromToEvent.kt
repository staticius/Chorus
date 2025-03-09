package org.chorus.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class BlockFromToEvent(block: Block, @JvmField var to: Block) : BlockEvent(block), Cancellable {
    val from: Block?
        get() = getBlock()

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}