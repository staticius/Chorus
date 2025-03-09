package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCrimsonStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Crimson Wood Stairs"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    companion object {
        val properties: BlockProperties = BlockProperties(
            CRIMSON_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}