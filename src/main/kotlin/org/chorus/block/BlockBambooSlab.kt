package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBambooSlab(blockState: BlockState?) : BlockWoodenSlab(blockState, BAMBOO_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Bamboo"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BAMBOO_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}