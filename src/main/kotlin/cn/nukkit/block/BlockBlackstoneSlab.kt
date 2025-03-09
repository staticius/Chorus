package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockBlackstoneSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, BLACKSTONE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Blackstone"
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

    companion object {
        val properties: BlockProperties =
            BlockProperties(BLACKSTONE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}