package org.chorus.block

class BlockInfoUpdate2 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.INFO_UPDATE2)
    }
}