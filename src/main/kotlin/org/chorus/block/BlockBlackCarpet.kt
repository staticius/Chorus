package org.chorus.block

class BlockBlackCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_CARPET)
            get() = Companion.field
    }
}