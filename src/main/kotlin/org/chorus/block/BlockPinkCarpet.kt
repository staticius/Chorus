package org.chorus.block

class BlockPinkCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_CARPET)
            get() = Companion.field
    }
}