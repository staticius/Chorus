package org.chorus.block

class BlockCutSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CUT_SANDSTONE)

    }
}
