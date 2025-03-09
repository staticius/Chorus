package org.chorus.block

class BlockCyanConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_CONCRETE)
            get() = Companion.field
    }
}