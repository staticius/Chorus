package org.chorus.block

class BlockLightBlueTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_TERRACOTTA)
            get() = Companion.field
    }
}