package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWaxedOxidizedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockOxidizedDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState
        get() = BlockWaxedOxidizedCutCopperSlab.Companion.PROPERTIES.getDefaultState()

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            CommonBlockProperties.MINECRAFT_VERTICAL_HALF
        )
            get() = Companion.field
    }
}