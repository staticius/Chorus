package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSmoothQuartz @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMOOTH_QUARTZ, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
