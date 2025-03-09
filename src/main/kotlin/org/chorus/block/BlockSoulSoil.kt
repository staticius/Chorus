package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockSoulSoil @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Soul Soil"

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 1.0

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override fun canHarvestWithHand(): Boolean {
        return true
    }

    override val isSoulSpeedCompatible: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SOUL_SOIL)
            get() = Companion.field
    }
}
