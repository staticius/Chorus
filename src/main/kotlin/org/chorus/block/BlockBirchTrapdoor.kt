package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Birch Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BIRCH_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}