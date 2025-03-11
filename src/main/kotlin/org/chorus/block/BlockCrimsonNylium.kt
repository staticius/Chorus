package org.chorus.block

class BlockCrimsonNylium @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockNylium(blockstate) {
    override val name: String
        get() = "Crimson Nylium"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRIMSON_NYLIUM)

    }
}