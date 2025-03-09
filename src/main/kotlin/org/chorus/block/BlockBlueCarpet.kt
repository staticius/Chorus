package org.chorus.block

class BlockBlueCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_CARPET)
            get() = Companion.field
    }
}