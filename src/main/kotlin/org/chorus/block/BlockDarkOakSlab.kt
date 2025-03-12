package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, DARK_OAK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Dark Oak"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DARK_OAK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}