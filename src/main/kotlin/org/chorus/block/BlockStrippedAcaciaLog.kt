package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedAcaciaLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    override fun getStrippedState(): BlockState {
        return Companion.properties.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_ACACIA_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}