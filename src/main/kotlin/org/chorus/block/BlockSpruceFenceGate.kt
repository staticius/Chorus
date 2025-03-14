package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Spruce Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.SPRUCE_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )

    }
}