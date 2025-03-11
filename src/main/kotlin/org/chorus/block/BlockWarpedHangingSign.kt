package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockWarpedHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Warped Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.WARPED_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            
    }
}