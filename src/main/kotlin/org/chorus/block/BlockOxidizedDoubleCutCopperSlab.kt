package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

open class BlockOxidizedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDoubleCutCopperSlab(blockstate) {
    override fun getSingleSlab() = BlockOxidizedCutCopperSlab.properties.defaultState

    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}