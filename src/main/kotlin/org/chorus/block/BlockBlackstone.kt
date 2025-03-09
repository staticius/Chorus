package org.chorus.block

import cn.nukkit.item.ItemTool

open class BlockBlackstone @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Blackstone"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    companion object {
        val properties: BlockProperties = BlockProperties(BLACKSTONE)
            get() = Companion.field
    }
}
