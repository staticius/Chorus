package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, BIRCH_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Birch"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BIRCH_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}