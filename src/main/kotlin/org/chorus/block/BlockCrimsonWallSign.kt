package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockCrimsonWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return CRIMSON_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return CRIMSON_STANDING_SIGN
    }

    override fun toItem(): Item? {
        return ItemCrimsonSign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
