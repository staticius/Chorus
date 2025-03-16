package org.chorus.block

class BlockElement1 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID."minecraft:element_1")

    }
}