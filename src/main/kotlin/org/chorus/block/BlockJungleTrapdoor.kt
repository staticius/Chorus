package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Jungle Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.Companion.JUNGLE_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}