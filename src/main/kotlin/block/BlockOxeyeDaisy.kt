package org.chorus_oss.chorus.block

class BlockOxeyeDaisy : BlockFlower {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXEYE_DAISY)
    }
}