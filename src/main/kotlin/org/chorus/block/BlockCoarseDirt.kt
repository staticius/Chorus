package org.chorus.block

class BlockCoarseDirt @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDirt(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(COARSE_DIRT)
            get() = Companion.field
    }
}