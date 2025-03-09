package org.chorus.block

class BlockLightBlueCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_CARPET)
            get() = Companion.field
    }
}