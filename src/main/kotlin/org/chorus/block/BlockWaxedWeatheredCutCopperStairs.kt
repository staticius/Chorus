package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWaxedWeatheredCutCopperStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWeatheredCutCopperStairs(blockstate) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_WEATHERED_CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}