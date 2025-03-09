package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockAcaciaLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}