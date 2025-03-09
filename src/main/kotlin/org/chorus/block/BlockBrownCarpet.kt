package org.chorus.block

class BlockBrownCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_CARPET)
            get() = Companion.field
    }
}