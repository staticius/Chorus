package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWarpedHyphae @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.getDefaultState()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_HYPHAE, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}