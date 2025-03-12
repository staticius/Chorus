package org.chorus.block

class BlockRedTulip : BlockFlower {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_TULIP)

    }
}