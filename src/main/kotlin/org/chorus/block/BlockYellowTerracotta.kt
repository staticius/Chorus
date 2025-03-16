package org.chorus.block

class BlockYellowTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_TERRACOTTA)

    }
}