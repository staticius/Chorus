package org.chorus.block

class BlockCornflower : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(CORNFLOWER)
            get() = Companion.field
    }
}