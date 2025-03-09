package org.chorus.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

/**
 * @author CreeperFace
 * @since 12.5.2017
 */
class BlockRedstoneEvent(block: Block, val oldPower: Int, val newPower: Int) : BlockEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
