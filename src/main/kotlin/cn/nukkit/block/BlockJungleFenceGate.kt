package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockJungleFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Jungle Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.Companion.JUNGLE_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}