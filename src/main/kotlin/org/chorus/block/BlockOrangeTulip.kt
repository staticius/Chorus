package org.chorus.block

class BlockOrangeTulip : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_TULIP)
            get() = Companion.field
    }
}