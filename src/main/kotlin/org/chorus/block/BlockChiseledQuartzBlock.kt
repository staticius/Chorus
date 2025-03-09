package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockChiseledQuartzBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockQuartzBlock(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_QUARTZ_BLOCK, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
