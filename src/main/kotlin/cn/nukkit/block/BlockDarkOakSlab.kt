package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDarkOakSlab(blockstate: BlockState?) : BlockWoodenSlab(blockstate, DARK_OAK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Dark Oak"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DARK_OAK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}