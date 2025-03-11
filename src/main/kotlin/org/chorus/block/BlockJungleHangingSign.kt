package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockJungleHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Jungle Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.Companion.JUNGLE_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )

    }
}