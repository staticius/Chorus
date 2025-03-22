package org.chorus.block

class BlockMagentaConcrete @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CONCRETE)
    }
}