package org.chorus.block

import org.chorus.item.ItemID
import org.chorus.item.ItemTool

open class BlockIronOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockOre(blockstate) {
    override val name: String
        get() = "Iron Ore"

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun getRawMaterial(): String? {
        return ItemID.RAW_IRON
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.IRON_ORE)

    }
}