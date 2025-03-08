package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
class BlockSpreadEvent(block: Block, val source: Block, newState: Block?) :
    BlockFormEvent(block, newState) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
