package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemCherrySign

class BlockCherryWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.CHERRY_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.CHERRY_STANDING_SIGN
    }

    override fun toItem(): Item {
        return ItemCherrySign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CHERRY_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
    }
}
