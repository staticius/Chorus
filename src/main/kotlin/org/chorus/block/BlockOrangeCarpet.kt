package org.chorus.block

class BlockOrangeCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_CARPET)
            get() = Companion.field
    }
}