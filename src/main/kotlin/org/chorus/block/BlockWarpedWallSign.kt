package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item

class BlockWarpedWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockWallSign(blockState) {
    override val wallSignId: String
        get() = BlockID.WARPED_WALL_SIGN

    override val standingSignId: String?
        get() = BlockID.WARPED_STANDING_SIGN

    override val name: String
        get() = "Warped Wall Sign"

    override fun toItem(): Item? {
        return ItemWarpedSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
