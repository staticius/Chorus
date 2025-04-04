package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedExposedCutCopperStairs @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockExposedCutCopperStairs(blockstate) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}