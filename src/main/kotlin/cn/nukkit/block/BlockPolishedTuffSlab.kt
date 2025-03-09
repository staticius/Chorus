package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedTuffSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, BlockID.POLISHED_TUFF_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Tuff"
    }

    override val name: String
        get() = "Polished Tuff Slab"

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 1.5

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_TUFF_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}