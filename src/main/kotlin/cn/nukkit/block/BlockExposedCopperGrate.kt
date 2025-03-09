package cn.nukkit.block

import cn.nukkit.block.property.enums.OxidizationLevel

class BlockExposedCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(EXPOSED_COPPER_GRATE)
            get() = Companion.field
    }
}