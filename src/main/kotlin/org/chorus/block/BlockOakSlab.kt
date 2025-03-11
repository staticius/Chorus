package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOakSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, BlockID.OAK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Oak"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OAK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}