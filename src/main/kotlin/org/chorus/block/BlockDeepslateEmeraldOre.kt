package org.chorus.block

class BlockDeepslateEmeraldOre @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockEmeraldOre(blockstate) {
    override val name: String
        get() = "Deepslate Emerald Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEEPSLATE_EMERALD_ORE)

    }
}