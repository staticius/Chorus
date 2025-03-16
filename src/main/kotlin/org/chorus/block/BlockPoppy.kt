package org.chorus.block

class BlockPoppy : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POPPY)

    }
}