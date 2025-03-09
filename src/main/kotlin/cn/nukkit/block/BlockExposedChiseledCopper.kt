package cn.nukkit.block

import cn.nukkit.block.property.enums.OxidizationLevel

class BlockExposedChiseledCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(EXPOSED_CHISELED_COPPER)
            get() = Companion.field
    }
}