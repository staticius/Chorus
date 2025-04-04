package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemWarpedSign

class BlockWarpedWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId() = BlockID.WARPED_WALL_SIGN

    override fun getStandingSignId() = BlockID.WARPED_STANDING_SIGN

    override val name: String
        get() = "Warped Wall Sign"

    override fun toItem(): Item {
        return ItemWarpedSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}
