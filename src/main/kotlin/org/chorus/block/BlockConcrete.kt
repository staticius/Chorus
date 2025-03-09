package org.chorus.block

import org.chorus.item.ItemTool

/**
 * @author CreeperFace
 * @since 2.6.2017
 */
abstract class BlockConcrete(blockState: BlockState?) : BlockSolid(blockState) {
    override val resistance: Double
        get() = 9.0

    override val hardness: Double
        get() = 1.8

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN
}
