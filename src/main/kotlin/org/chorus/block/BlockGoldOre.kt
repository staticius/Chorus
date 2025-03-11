package org.chorus.block

import org.chorus.item.ItemID
import org.chorus.item.ItemTool

open class BlockGoldOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockOre(blockstate) {
    override val name: String
        get() = "Gold Ore"

    override fun getRawMaterial(): String? {
        return ItemID.RAW_GOLD
    }

    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GOLD_ORE)

    }
}