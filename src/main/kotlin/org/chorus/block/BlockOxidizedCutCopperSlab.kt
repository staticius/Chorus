package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopperSlab : BlockCutCopperSlab {
    @JvmOverloads
    constructor(blockstate: BlockState = Companion.properties.defaultState) : super(
        blockstate,
        BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB
    )

    protected constructor(blockstate: BlockState, doubleSlabId: String?) : super(blockstate, doubleSlabId)

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OXIDIZED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}