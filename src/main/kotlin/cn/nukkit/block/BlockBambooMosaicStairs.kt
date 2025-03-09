package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockBambooMosaicStairs(blockState: BlockState?) : BlockStairs(blockState) {
    override val name: String
        get() = "Bamboo Mosaic Stairs"

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
            BAMBOO_MOSAIC_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}