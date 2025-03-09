package cn.nukkit.block

class BlockWaxedWeatheredCutCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWeatheredCutCopper(blockstate) {
    override val name: String
        get() = "Waxed Weathered Cut Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_WEATHERED_CUT_COPPER)
            get() = Companion.field
    }
}