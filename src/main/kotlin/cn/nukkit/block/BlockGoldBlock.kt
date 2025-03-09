package cn.nukkit.block

import cn.nukkit.item.ItemTool

class BlockGoldBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Gold Block"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 30.0

    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(GOLD_BLOCK)
            get() = Companion.field
    }
}