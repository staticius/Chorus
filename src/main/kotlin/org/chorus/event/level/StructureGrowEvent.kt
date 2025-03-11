package org.chorus.event.level

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import java.util.*

/**
 * @author KCodeYT (Nukkit Project)
 */
class StructureGrowEvent(val block: Block, private val blocks: MutableList<Block>) :
    LevelEvent(Objects.requireNonNull(block.level)), Cancellable {
    var blockList: List<Block>
        get() = this.blocks
        set(blocks) {
            this.blocks.clear()
            this.blocks.addAll(blocks)
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
