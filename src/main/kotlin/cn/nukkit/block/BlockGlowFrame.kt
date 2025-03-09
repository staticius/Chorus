package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntity

class BlockGlowFrame @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFrame(blockstate) {
    override val name: String
        get() = "Glow Item Frame"

    override val blockEntityType: String
        get() = BlockEntity.GLOW_ITEM_FRAME

    companion object {
        val properties: BlockProperties = BlockProperties(
            GLOW_FRAME,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.ITEM_FRAME_MAP_BIT,
            CommonBlockProperties.ITEM_FRAME_PHOTO_BIT
        )
            get() = Companion.field
    }
}