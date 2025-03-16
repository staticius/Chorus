package org.chorus.block

class BlockBlueOrchid : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_ORCHID)
    }
}