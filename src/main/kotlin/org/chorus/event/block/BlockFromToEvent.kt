package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class BlockFromToEvent(block: Block, @JvmField var to: Block) : BlockEvent(block), Cancellable {
    val from: Block
        get() = block

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}