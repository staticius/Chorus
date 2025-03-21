package org.chorus.block

class BlockGrayCarpet @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_CARPET)
    }
}