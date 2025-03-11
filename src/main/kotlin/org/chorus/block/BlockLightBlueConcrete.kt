package org.chorus.block

class BlockLightBlueConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_CONCRETE)

    }
}