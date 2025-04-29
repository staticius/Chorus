package org.chorus_oss.chorus.block

class BlockDarkPrismarine : BlockPrismarine {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_PRISMARINE)
    }
}
