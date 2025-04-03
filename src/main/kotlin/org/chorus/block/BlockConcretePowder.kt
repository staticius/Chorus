package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex

abstract class BlockConcretePowder(blockState: BlockState) : BlockFallable(blockState) {
    override val resistance: Double
        get() = 2.5

    override val hardness: Double
        get() = 0.5

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    abstract fun getConcrete(): BlockConcrete

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            super.onUpdate(Level.BLOCK_UPDATE_NORMAL)

            for (side in 1..5) {
                val block = this.getSide(fromIndex(side))
                if (block.id == BlockID.FLOWING_WATER || block.id == BlockID.WATER) {
                    level.setBlock(this.position, getConcrete(), direct = true, update = true)
                }
            }

            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override fun place(
        item: Item?,
        b: Block,
        target: Block?,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        var concrete = false

        for (side in 1..5) {
            val block = this.getSide(fromIndex(side))
            if (block.id == BlockID.FLOWING_WATER || block.id == BlockID.WATER) {
                concrete = true
                break
            }
        }

        if (concrete) {
            level.setBlock(this.position, this.getConcrete(), direct = true, update = true)
        } else {
            level.setBlock(this.position, this, direct = true, update = true)
        }

        return true
    }
}
