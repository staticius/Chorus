package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockDeepslateBrickSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, DEEPSLATE_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Deepslate Brick"
    }

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEEPSLATE_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}