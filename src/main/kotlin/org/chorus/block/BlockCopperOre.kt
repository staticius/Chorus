package org.chorus.block

import org.chorus.item.ItemID
import org.chorus.item.ItemTool

open class BlockCopperOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockOre(blockstate) {
    override val name: String
        get() = "Copper Ore"

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun getRawMaterial(): String? {
        return ItemID.RAW_COPPER
    }

    override fun getDropMultiplier(): Float {
        return 3f
    }

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 3.0

    companion object {
        val properties: BlockProperties = BlockProperties(COPPER_ORE)
            get() = Companion.field
    }
}