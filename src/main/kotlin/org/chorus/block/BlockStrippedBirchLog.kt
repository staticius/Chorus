package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedBirchLog @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWoodStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.properties.defaultState
    }

    override fun getWoodType(): WoodType {
        return WoodType.BIRCH
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STRIPPED_BIRCH_LOG, CommonBlockProperties.PILLAR_AXIS)
    }
}