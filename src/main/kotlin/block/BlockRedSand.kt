package org.chorus_oss.chorus.block

class BlockRedSand : BlockSand {
    constructor() : super(properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_SAND)
    }
}
