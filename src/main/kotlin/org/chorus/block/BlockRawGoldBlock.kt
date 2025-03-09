package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockRawGoldBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockRaw(blockstate) {
    override val name: String
        get() = "Block of Raw Gold"

    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RAW_GOLD_BLOCK)
            get() = Companion.field
    }
}