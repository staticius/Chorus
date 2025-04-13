package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaHangingSign(blockState: BlockState = Companion.properties.defaultState) : BlockHangingSign(blockState) {
    override val name: String
        get() = "Acacia Hanging Sign"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.ACACIA_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
    }
}