package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockCrimsonWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.CRIMSON_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.CRIMSON_STANDING_SIGN
    }

    override fun toItem(): Item {
        return ItemCrimsonSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CRIMSON_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
    }
}
