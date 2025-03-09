package org.chorus.block

class BlockCrimsonFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Crimson Fence"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_FENCE)
            get() = Companion.field
    }
}