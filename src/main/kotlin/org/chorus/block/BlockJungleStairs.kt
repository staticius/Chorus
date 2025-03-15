package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Jungle Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.Companion.JUNGLE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}