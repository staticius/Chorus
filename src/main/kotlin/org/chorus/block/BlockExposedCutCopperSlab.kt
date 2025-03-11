package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

open class BlockExposedCutCopperSlab : BlockCutCopperSlab {
    @JvmOverloads
    constructor(blockstate: BlockState? = Companion.properties.defaultState) : super(
        blockstate,
        EXPOSED_DOUBLE_CUT_COPPER_SLAB
    )

    protected constructor(blockstate: BlockState?, doubleSlabId: String?) : super(blockstate, doubleSlabId)

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.EXPOSED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}