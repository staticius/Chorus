package org.chorus_oss.chorus.block

class BlockInfoUpdate : Block {
    constructor() : super(properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Info Update Block"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.INFO_UPDATE)
    }
}
