package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockNetherBrickFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Nether Brick Fence"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 6.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_BRICK_FENCE)
            get() = Companion.field
    }
}