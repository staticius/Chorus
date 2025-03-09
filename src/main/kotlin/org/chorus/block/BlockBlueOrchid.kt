package org.chorus.block

class BlockBlueOrchid : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_ORCHID)
            get() = Companion.field
    }
}