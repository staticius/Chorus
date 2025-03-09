package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWaxedExposedCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    override val lightLevel: Int
        get() = if (lit) 12 else 0

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_EXPOSED_COPPER_BULB,
            CommonBlockProperties.LIT,
            CommonBlockProperties.POWERED_BIT
        )
            get() = Companion.field
    }
}