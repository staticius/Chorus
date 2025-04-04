package org.chorus.block

class BlockWarpedNylium @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockNylium(blockstate) {
    override val name: String
        get() = "Warped Nylium"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_NYLIUM)

    }
}