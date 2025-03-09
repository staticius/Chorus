package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex

/**
 * @author CreeperFace
 * @since 2.6.2017
 */
abstract class BlockConcretePowder(blockState: BlockState?) : BlockFallable(blockState) {
    override val resistance: Double
        get() = 2.5

    override val hardness: Double
        get() = 0.5

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    abstract val concrete: BlockConcrete

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            super.onUpdate(Level.BLOCK_UPDATE_NORMAL)

            for (side in 1..5) {
                val block = this.getSide(fromIndex(side)!!)
                if (block!!.id == Block.FLOWING_WATER || block.id == Block.WATER) {
                    level.setBlock(this.position, concrete, true, true)
                }
            }

            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override fun place(
        item: Item,
        b: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        var concrete = false

        for (side in 1..5) {
            val block = this.getSide(fromIndex(side)!!)
            if (block!!.id == Block.FLOWING_WATER || block.id == Block.WATER) {
                concrete = true
                break
            }
        }

        if (concrete) {
            level.setBlock(this.position, this.concrete, true, true)
        } else {
            level.setBlock(this.position, this, true, true)
        }

        return true
    }
}
