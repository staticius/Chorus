package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockAcaciaWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return ACACIA_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return ACACIA_STANDING_SIGN
    }

    override val name: String
        get() = "Acacia Wall Sign"

    override fun toItem(): Item? {
        return ItemAcaciaSign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
