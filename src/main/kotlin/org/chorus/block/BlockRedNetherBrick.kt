package org.chorus.block

class BlockRedNetherBrick @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockNetherBrick(blockstate) {
    override val name: String
        get() = "Red Nether Bricks"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_NETHER_BRICK)

    }
}