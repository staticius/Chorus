package org.chorus.block

class BlockWhiteTulip : BlockFlower {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState) :  super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_TULIP)

    }
}