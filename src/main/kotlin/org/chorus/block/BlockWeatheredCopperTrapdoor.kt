package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWeatheredCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperTrapdoor(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WEATHERED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}