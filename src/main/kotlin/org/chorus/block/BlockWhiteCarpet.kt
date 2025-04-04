package org.chorus.block

class BlockWhiteCarpet @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CARPET)

    }
}