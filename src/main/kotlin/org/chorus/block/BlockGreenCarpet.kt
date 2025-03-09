package org.chorus.block

class BlockGreenCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_CARPET)
            get() = Companion.field
    }
}