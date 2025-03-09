package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedOxidizedCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockOxidizedCutCopperSlab(blockstate, BlockID.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_OXIDIZED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}