package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakStairs @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Dark Oak Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.DARK_OAK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}