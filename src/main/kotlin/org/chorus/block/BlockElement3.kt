package org.chorus.block

class BlockElement3 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID."minecraft:element_3")

    }
}