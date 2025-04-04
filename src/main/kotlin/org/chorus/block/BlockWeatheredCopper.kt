package org.chorus.block

open class BlockWeatheredCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBlock(blockstate) {
    override val name: String
        get() = "Weathered Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WEATHERED_COPPER)

    }
}