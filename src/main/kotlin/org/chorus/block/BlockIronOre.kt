package org.chorus.block

import org.chorus.item.ItemID
import org.chorus.item.ItemTool

open class BlockIronOre @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockOre(blockstate) {
    override val name: String
        get() = "Iron Ore"

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override val rawMaterial: String?
        get() = ItemID.RAW_IRON

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.IRON_ORE)
    }
}