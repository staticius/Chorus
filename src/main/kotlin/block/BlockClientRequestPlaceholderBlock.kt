package org.chorus_oss.chorus.block

class BlockClientRequestPlaceholderBlock @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CLIENT_REQUEST_PLACEHOLDER_BLOCK)
    }
}