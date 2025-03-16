package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool


class BlockMudBrickSlab @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockSlab(blockState, BlockID.MUD_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Mud Brick"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return slab.id == id
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 3.0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MUD_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}
