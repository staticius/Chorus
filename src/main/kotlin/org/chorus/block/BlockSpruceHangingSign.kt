package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockSpruceHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Spruce Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SPRUCE_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}