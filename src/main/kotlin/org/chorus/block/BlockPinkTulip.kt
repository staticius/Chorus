package org.chorus.block

class BlockPinkTulip : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_TULIP)
            get() = Companion.field
    }
}