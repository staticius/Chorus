package org.chorus.block

class BlockGreenTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_TERRACOTTA)
            get() = Companion.field
    }
}