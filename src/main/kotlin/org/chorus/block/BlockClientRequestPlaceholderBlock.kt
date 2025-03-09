package org.chorus.block

class BlockClientRequestPlaceholderBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CLIENT_REQUEST_PLACEHOLDER_BLOCK)
            get() = Companion.field
    }
}