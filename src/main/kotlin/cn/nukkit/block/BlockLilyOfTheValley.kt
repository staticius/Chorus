package cn.nukkit.block

class BlockLilyOfTheValley : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LILY_OF_THE_VALLEY)
            get() = Companion.field
    }
}