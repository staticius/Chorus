package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockTuffDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Tuff"

    override val singleSlab: BlockState
        get() = BlockTuffSlab.Companion.PROPERTIES.getDefaultState()

    override val name: String
        get() = "Tuff Double Slab"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TUFF_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}