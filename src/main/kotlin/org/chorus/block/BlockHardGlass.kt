package org.chorus.block

class BlockHardGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GLASS)
    }
}