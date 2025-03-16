package org.chorus.block

class BlockInfoUpdate : Block {
    constructor() : super(Companion.properties.defaultState,)

    constructor(blockState: BlockState) : super(blockState,)

    override val name: String
        get() = "Info Update Block"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.INFO_UPDATE)

    }
}
