package org.chorus.block

class BlockWhiteConcrete @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CONCRETE)

    }
}