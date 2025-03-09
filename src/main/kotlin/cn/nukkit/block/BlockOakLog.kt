package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockOakLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedOakLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}