package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedBlackstoneBrickSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSlab(blockstate, BlockID.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Blackstone"
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

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_BLACKSTONE_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}