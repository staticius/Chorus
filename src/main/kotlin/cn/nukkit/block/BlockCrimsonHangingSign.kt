package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockCrimsonHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Crimson Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            CRIMSON_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}