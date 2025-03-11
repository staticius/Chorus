package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedSpruceLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.PROPERTIES.getDefaultState()
    }

    override fun getWoodType(): WoodType {
        return WoodType.SPRUCE
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_SPRUCE_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}