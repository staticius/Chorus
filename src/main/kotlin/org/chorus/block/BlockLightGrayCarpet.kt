package org.chorus.block

class BlockLightGrayCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_CARPET)

    }
}