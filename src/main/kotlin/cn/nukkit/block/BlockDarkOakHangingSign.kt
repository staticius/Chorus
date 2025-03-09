package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockDarkOakHangingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHangingSign(blockstate) {
    override val name: String
        get() = "Dark Oak Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            DARK_OAK_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}