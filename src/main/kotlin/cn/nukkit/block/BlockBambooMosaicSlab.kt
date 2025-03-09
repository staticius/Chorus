package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBambooMosaicSlab(blockState: BlockState?) : BlockWoodenSlab(blockState, BAMBOO_MOSAIC_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Bamboo Mosaic"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BAMBOO_MOSAIC_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}