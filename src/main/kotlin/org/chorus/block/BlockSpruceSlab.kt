package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, BlockID.SPRUCE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Spruce"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}