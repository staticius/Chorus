package org.chorus.block

class BlockAzureBluet : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(AZURE_BLUET)
            get() = Companion.field
    }
}