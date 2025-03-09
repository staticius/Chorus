package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockSilverGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SILVER_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}