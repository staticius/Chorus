package org.chorus.block

class BlockYellowConcrete @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CONCRETE)

    }
}