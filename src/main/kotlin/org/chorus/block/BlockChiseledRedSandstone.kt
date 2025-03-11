package org.chorus.block

class BlockChiseledRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_RED_SANDSTONE)

    }
}
