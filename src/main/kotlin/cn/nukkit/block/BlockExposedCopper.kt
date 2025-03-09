package cn.nukkit.block

import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockExposedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBlock(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(EXPOSED_COPPER)
            get() = Companion.field
    }
}