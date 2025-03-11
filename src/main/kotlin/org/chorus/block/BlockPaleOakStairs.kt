package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Pale Oak Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.PALE_OAK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}