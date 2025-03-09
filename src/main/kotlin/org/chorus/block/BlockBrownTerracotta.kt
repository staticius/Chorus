package org.chorus.block

class BlockBrownTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_TERRACOTTA)
            get() = Companion.field
    }
}