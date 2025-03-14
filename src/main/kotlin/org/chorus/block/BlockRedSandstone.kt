package org.chorus.block

class BlockRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_SANDSTONE)

    }
}
