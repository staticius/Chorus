package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockOxidizedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState?
        get() = BlockOxidizedCutCopperSlab.Companion.PROPERTIES.getDefaultState()

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}