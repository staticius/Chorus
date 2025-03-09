package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBambooSlab(blockState: BlockState?) : BlockWoodenSlab(blockState, BAMBOO_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Bamboo"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BAMBOO_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}