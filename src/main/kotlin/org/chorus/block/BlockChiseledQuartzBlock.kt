package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockChiseledQuartzBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockQuartzBlock(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_QUARTZ_BLOCK, CommonBlockProperties.PILLAR_AXIS)
            
    }
}
