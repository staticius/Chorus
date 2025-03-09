package org.chorus.block

class BlockYellowCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CARPET)
            get() = Companion.field
    }
}