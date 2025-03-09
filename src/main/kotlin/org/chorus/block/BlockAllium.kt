package org.chorus.block

class BlockAllium : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(ALLIUM)
            get() = Companion.field
    }
}