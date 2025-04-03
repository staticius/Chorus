package org.chorus.block

class BlockPinkTulip : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_TULIP)
    }
}