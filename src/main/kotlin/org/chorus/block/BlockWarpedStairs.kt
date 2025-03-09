package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWarpedStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Warped Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WARPED_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}