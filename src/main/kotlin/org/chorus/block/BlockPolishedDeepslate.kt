package org.chorus.block

class BlockPolishedDeepslate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCobbledDeepslate(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_DEEPSLATE)
            get() = Companion.field
    }
}