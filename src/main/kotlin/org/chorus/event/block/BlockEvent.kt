package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Event


abstract class BlockEvent(protected val block: Block) : Event() {
    open fun getBlock(): Block {
        return block
    }
}
