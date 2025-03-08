package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class BlockFormEvent(block: Block, newState: Block?) :
    BlockGrowEvent(block, newState) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
