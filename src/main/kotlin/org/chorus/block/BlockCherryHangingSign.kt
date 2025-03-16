package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockCherryHangingSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockHangingSign(blockState) {
    override val name: String
        get() = "Cherry Hanging Sign"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.CHERRY_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
    }
}
