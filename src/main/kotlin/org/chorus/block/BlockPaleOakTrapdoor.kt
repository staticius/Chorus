package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPaleOakTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Pale Oak Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.PALE_OAK_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}