package org.chorus.block

class BlockChiseledSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_SANDSTONE)
            get() = Companion.field
    }
}
