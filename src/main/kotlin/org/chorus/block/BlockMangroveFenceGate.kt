package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMangroveFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Mangrove Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.MANGROVE_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )

    }
}