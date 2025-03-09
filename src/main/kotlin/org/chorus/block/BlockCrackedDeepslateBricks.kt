package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockCrackedDeepslateBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Cracked Deepslate Bricks"

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    companion object {
        val properties: BlockProperties = BlockProperties(CRACKED_DEEPSLATE_BRICKS)
            get() = Companion.field
    }
}