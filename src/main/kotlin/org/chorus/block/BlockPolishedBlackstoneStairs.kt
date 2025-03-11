package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

open class BlockPolishedBlackstoneStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockBlackstoneStairs(blockstate) {
    override val name: String
        get() = "Polished Blackstone Stairs"

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.POLISHED_BLACKSTONE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}