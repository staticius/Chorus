package org.chorus.block

class BlockChiseledDeepslate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCobbledDeepslate(blockstate) {
    override val name: String
        get() = "Chiseled Deepslate"

    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_DEEPSLATE)
            get() = Companion.field
    }
}