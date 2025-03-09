package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockCherryHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Cherry Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            CHERRY_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}
