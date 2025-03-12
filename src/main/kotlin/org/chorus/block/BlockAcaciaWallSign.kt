package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemAcaciaSign

class BlockAcaciaWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.ACACIA_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.ACACIA_STANDING_SIGN
    }

    override val name: String
        get() = "Acacia Wall Sign"

    override fun toItem(): Item {
        return ItemAcaciaSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.ACACIA_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
    }
}
