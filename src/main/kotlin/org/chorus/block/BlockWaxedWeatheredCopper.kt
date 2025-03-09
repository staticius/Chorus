package org.chorus.block

class BlockWaxedWeatheredCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWeatheredCopper(blockstate) {
    override val name: String
        get() = "Waxed Weathered Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_WEATHERED_COPPER)
            get() = Companion.field
    }
}