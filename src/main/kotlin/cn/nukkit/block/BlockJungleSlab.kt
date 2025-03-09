package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockJungleSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, BlockID.Companion.JUNGLE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Jungle"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}