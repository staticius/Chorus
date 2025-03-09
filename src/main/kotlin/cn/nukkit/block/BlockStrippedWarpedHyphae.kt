package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockStrippedWarpedHyphae @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStemStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_WARPED_HYPHAE, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}