package org.chorus.block

class BlockPrismarineBricks : BlockPrismarine {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PRISMARINE_BRICKS)

    }
}
