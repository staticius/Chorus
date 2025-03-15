package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMangroveStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Mangrove Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}