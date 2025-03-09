package cn.nukkit.block

class BlockDeepslateDiamondOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDiamondOre(blockstate) {
    override val name: String
        get() = "Deepslate Diamond Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(DEEPSLATE_DIAMOND_ORE)
            get() = Companion.field
    }
}