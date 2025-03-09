package org.chorus.block

class BlockOpenEyeblossom : BlockClosedEyeblossom {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OPEN_EYEBLOSSOM)
            get() = Companion.field
    }
}