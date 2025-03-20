package org.chorus.block

class BlockCutSandstone @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CUT_SANDSTONE)
    }
}
