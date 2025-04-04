package org.chorus.block

class BlockWarpedFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Warped Fence"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_FENCE)
    }
}