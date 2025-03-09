package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

open class BlockWeatheredCutCopperSlab : BlockCutCopperSlab {
    @JvmOverloads
    constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) : super(
        blockstate,
        BlockID.WEATHERED_DOUBLE_CUT_COPPER_SLAB
    )

    protected constructor(blockstate: BlockState?, doubleSlab: String?) : super(blockstate, doubleSlab)

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WEATHERED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}