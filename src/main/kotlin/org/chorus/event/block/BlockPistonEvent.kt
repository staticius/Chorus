package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.block.BlockPistonBase
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.math.BlockFace

class BlockPistonEvent(
    piston: BlockPistonBase,
    val direction: BlockFace,
    private val blocks: List<Block>,
    val destroyedBlocks: List<Block>,
    val isExtending: Boolean
) :
    BlockEvent(piston), Cancellable {
    fun getBlocks(): List<Block> {
        return ArrayList(blocks)
    }

    override fun getBlock(): Block {
        return super.getBlock() as BlockPistonBase
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
