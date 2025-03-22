package org.chorus.block

class BlockMagentaCarpet @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CARPET)
    }
}