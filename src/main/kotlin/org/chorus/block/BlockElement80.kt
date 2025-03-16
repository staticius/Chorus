package org.chorus.block

class BlockElement80 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID."minecraft:element_80")

    }
}