package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockAncientDebris @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Ancient Derbris"

    override val toolTier: Int
        get() = ItemTool.TIER_DIAMOND

    override val resistance: Double
        get() = 1200.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 30.0

    override val isLavaResistant: Boolean
        get() = true

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ANCIENT_DEBRIS)
            get() = Companion.field
    }
}
