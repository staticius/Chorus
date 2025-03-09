package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCyanGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(CYAN_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}