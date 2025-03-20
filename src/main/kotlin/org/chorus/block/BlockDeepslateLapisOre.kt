package org.chorus.block

class BlockDeepslateLapisOre @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLapisOre(blockstate) {
    override val name: String
        get() = "Deepslate Lapis Ore"

    override val hardness: Double
        get() = 4.5

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEEPSLATE_LAPIS_ORE)
    }
}