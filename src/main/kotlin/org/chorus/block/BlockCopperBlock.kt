package org.chorus.block

open class BlockCopperBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBase(blockstate) {
    override val name: String
        get() = "Block of Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(COPPER_BLOCK)
            get() = Companion.field
    }
}