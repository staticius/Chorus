package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockAzaleaLeavesFlowered @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockAzaleaLeaves(blockState) {
    override val name: String
        get() = "Azalea Leaves Flowered"

    companion object {
        val properties: BlockProperties = BlockProperties(
            AZALEA_LEAVES_FLOWERED,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )
            get() = Companion.field
    }
}
