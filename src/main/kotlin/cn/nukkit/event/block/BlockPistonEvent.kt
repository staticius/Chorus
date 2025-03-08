package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.block.BlockPistonBase
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.math.BlockFace

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

    override fun getBlock(): BlockPistonBase? {
        return super.getBlock() as BlockPistonBase
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
