package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

open class BlockWeatheredCutCopperSlab : BlockCutCopperSlab {
    @JvmOverloads
    constructor(blockstate: BlockState = Companion.properties.defaultState) : super(
        blockstate,
        BlockID.WEATHERED_DOUBLE_CUT_COPPER_SLAB
    )

    protected constructor(blockstate: BlockState, doubleSlab: String) : super(blockstate, doubleSlab)

    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.WEATHERED
        }
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WEATHERED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}