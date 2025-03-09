package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Birch Wood Stairs"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BIRCH_STAIRS, CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION)
            get() = Companion.field
    }
}