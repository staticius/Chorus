package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, BlockID.PALE_OAK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Pale Oak"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}