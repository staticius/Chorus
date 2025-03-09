package org.chorus.block

class BlockGrayCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_CARPET)
            get() = Companion.field
    }
}