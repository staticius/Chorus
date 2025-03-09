package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockSpruceDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Spruce"

    override val singleSlab: BlockState
        get() = BlockSpruceSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}