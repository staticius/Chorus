package cn.nukkit.block

class BlockDeepslateIronOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockIronOre(blockstate) {
    override val name: String
        get() = "Deepslate Iron Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(DEEPSLATE_IRON_ORE)
            get() = Companion.field
    }
}