package org.chorus.block

class BlockPolishedBlackstoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPolishedBlackstone(blockstate) {
    override val name: String
        get() = "Polished Blackstone Bricks"

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_BLACKSTONE_BRICKS)
            get() = Companion.field
    }
}