package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBirchStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Birch Wood Stairs"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BIRCH_STAIRS, CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION)

    }
}