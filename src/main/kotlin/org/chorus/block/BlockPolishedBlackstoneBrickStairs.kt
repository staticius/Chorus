package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPolishedBlackstoneBrickStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPolishedBlackstoneStairs(blockstate) {
    override val name: String
        get() = "Polished Blackstone Brick Stairs"

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_BLACKSTONE_BRICK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}