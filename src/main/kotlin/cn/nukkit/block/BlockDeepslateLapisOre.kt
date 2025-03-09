package cn.nukkit.block

class BlockDeepslateLapisOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLapisOre(blockstate) {
    override val name: String
        get() = "Deepslate Lapis Ore"

    override val hardness: Double
        get() = 4.5

    companion object {
        val properties: BlockProperties = BlockProperties(DEEPSLATE_LAPIS_ORE)
            get() = Companion.field
    }
}