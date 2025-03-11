package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOakStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Oak Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.OAK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            
    }
}