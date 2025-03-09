package org.chorus.event.level

import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import java.util.*

/**
 * @author KCodeYT (Nukkit Project)
 */
class StructureGrowEvent(val block: Block, private val blocks: MutableList<Block>) :
    LevelEvent(Objects.requireNonNull(block.level)), Cancellable {
    var blockList: List<Block>?
        get() = this.blocks
        set(blocks) {
            this.blocks.clear()
            if (blocks != null) this.blocks.addAll(blocks)
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
