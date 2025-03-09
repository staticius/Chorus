package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCrimsonHyphae @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_HYPHAE, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}