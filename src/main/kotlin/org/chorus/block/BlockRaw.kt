package org.chorus.block

import org.chorus.item.ItemTool

/**
 * @author LoboMetalurgico
 * @since 08/06/2021
 */
abstract class BlockRaw(blockstate: BlockState?) : BlockSolid(blockstate) {
    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val isLavaResistant: Boolean
        get() = true
}
