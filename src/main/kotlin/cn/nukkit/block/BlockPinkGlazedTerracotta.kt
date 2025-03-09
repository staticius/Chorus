package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPinkGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PINK_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}