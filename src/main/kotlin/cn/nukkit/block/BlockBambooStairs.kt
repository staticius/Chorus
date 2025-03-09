package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockBambooStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "Bamboo Stairs"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 3.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 20

    companion object {
        val properties: BlockProperties = BlockProperties(
            BAMBOO_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}