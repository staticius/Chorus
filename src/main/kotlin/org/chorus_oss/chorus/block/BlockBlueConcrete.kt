package org.chorus_oss.chorus.block

class BlockBlueConcrete @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_CONCRETE)
    }
}