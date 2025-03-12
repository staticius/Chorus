package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPolishedBlackstoneBrickDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPolishedBlackstoneDoubleSlab(blockstate) {
    override val slabName: String
        get() = "Polished Blackstone Brick"

    override val hardness: Double
        get() = 2.0

    override val singleSlab: BlockState
        get() = BlockPolishedBlackstoneBrickSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB,
            CommonBlockProperties.MINECRAFT_VERTICAL_HALF
        )

    }
}