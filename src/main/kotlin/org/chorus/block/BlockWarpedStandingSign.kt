package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item

class BlockWarpedStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStandingSign(blockstate) {
    override val wallSignId: String
        get() = BlockWarpedWallSign.Companion.PROPERTIES.getIdentifier()

    override fun toItem(): Item? {
        return ItemWarpedSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}