package org.chorus.block

class BlockDeepslateCoalOre @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCoalOre(blockstate) {
    override val name: String
        get() = "Deeplsate Coal Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEEPSLATE_COAL_ORE)

    }
}