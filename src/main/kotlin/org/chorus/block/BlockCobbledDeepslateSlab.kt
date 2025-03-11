package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockCobbledDeepslateSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, COBBLED_DEEPSLATE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Cobbled Deepslate"
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 3.5

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.COBBLED_DEEPSLATE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}