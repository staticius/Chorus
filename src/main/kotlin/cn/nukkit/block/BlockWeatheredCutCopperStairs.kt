package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

open class BlockWeatheredCutCopperStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCutCopperStairs(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WEATHERED_CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}