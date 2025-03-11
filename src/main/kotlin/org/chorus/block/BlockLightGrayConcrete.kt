package org.chorus.block

class BlockLightGrayConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_CONCRETE)

    }
}