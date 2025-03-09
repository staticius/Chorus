package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockAcaciaFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Acacia Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            ACACIA_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}