package org.chorus_oss.chorus.block

class BlockOpenEyeblossom : BlockClosedEyeblossom {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OPEN_EYEBLOSSOM)

    }
}