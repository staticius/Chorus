package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockStrippedWarpedHyphae @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStemStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.properties.defaultState
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_WARPED_HYPHAE, CommonBlockProperties.PILLAR_AXIS)
    }
}