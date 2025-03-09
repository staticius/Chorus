package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopperStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCutCopperStairs(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.OXIDIZED_CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}