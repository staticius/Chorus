package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCherrySlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenSlab(blockstate, CHERRY_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Cherry"
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}