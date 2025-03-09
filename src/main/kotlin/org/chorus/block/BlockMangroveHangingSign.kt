package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockMangroveHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Mangrove Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}