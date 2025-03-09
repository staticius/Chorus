package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWhiteGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WHITE_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}