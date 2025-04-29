package org.chorus_oss.chorus.block

class BlockLilyOfTheValley : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LILY_OF_THE_VALLEY)
    }
}