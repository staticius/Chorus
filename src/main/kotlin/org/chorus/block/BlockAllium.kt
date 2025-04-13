package org.chorus.block

class BlockAllium(blockState: BlockState = Companion.properties.defaultState) : BlockFlower(blockState) {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ALLIUM)
    }
}