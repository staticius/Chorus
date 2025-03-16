package org.chorus.block

class BlockCornflower(blockstate: BlockState = Companion.properties.defaultState) : BlockFlower(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CORNFLOWER)
    }
}