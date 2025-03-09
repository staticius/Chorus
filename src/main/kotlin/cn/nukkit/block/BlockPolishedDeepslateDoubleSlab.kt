package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedDeepslateDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Polished Deepslate"

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

    override val singleSlab: BlockState
        get() = BlockPolishedDeepslateSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_DEEPSLATE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}