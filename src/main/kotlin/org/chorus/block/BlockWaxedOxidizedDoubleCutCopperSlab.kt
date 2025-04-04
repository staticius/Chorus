package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedOxidizedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockOxidizedDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState
        get() = BlockWaxedOxidizedCutCopperSlab.Companion.properties.defaultState

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            CommonBlockProperties.MINECRAFT_VERTICAL_HALF
        )

    }
}