package org.chorus.block

class BlockDarkPrismarine : BlockPrismarine {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_PRISMARINE)

    }
}
