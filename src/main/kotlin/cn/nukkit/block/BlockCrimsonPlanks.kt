package cn.nukkit.block

class BlockCrimsonPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    override val name: String
        get() = "Crimson Planks"

    override val resistance: Double
        get() = 3.0

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_PLANKS)
            get() = Companion.field
    }
}