package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity

class BlockGlowFrame @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFrame(blockstate) {
    override val name: String
        get() = "Glow Item Frame"

    override fun getBlockEntityType(): String {
        return BlockEntity.GLOW_ITEM_FRAME

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.GLOW_FRAME,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.ITEM_FRAME_MAP_BIT,
            CommonBlockProperties.ITEM_FRAME_PHOTO_BIT
        )

    }
}