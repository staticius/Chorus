package org.chorus.block

class BlockRedSand : BlockSand {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockState: BlockState) : super(blockState)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_SAND)

    }
}
