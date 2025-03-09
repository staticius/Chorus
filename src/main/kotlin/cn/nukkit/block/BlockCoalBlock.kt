package cn.nukkit.block

import cn.nukkit.item.ItemTool

class BlockCoalBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 5

    override val name: String
        get() = "Block of Coal"

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(COAL_BLOCK)
            get() = Companion.field
    }
}