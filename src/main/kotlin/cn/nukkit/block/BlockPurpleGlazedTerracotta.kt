package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPurpleGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PURPLE_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}