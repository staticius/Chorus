package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopperSlab(blockState: BlockState = properties.defaultState, doubleSlabID: String = BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB) : BlockCutCopperSlab(blockState, doubleSlabID) {
    constructor(blockState: BlockState) : this(blockState, BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB)

    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OXIDIZED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}