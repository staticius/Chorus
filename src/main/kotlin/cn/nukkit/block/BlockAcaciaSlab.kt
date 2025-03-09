package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockAcaciaSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, ACACIA_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Acacia"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}