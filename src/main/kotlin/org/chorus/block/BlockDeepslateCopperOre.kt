package org.chorus.block

class BlockDeepslateCopperOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperOre(blockstate) {
    override val name: String
        get() = "Deepslate Copper Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(DEEPSLATE_COPPER_ORE)
            get() = Companion.field
    }
}