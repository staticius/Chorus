package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockSculkVein @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockLichen(blockstate) {
    override val name: String
        get() = "Sculk Vein"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SCULK_VEIN, CommonBlockProperties.MULTI_FACE_DIRECTION_BITS)

    }
}
