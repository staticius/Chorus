package org.chorus.block

class BlockCamera @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CAMERA)

    }
}