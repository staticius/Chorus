package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBeeNest @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockBeehive(blockstate) {
    override val name: String
        get() = "Bee Nest"

    override val burnChance: Int
        get() = 30

    override val burnAbility: Int
        get() = 60

    override val hardness: Double
        get() = 0.3

    override val resistance: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties =
            BlockProperties(BEE_NEST, CommonBlockProperties.DIRECTION, CommonBlockProperties.HONEY_LEVEL)
            get() = Companion.field
    }
}
