package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Birch Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BIRCH_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}