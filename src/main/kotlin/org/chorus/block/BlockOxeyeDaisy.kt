package org.chorus.block

class BlockOxeyeDaisy : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXEYE_DAISY)
            get() = Companion.field
    }
}