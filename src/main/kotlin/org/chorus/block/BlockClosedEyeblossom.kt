package org.chorus.block

open class BlockClosedEyeblossom : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CLOSED_EYEBLOSSOM)

    }
}