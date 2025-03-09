package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

open class BlockCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Copper Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}