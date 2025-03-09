package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockCrimsonSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, CRIMSON_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Crimson"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
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

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}