package cn.nukkit.block

open class BlockClosedEyeblossom : BlockFlower {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties = BlockProperties(CLOSED_EYEBLOSSOM)
            get() = Companion.field
    }
}