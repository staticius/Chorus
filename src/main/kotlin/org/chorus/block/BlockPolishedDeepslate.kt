package org.chorus.block

class BlockPolishedDeepslate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCobbledDeepslate(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_DEEPSLATE)

    }
}