package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDarkOakFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Dark Oak Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            DARK_OAK_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}