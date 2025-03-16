package org.chorus.block

class BlockElement96 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_96")

    }
}