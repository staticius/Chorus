package org.chorus.block

class BlockGreenConcrete @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_CONCRETE)
    }
}