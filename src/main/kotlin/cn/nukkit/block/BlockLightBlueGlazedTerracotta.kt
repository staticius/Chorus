package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockLightBlueGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_BLUE_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}