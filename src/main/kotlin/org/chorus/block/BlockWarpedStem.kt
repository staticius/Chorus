package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWarpedStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.getDefaultState()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_STEM, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}