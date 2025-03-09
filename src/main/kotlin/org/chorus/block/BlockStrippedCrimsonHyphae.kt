package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockStrippedCrimsonHyphae @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStemStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_CRIMSON_HYPHAE, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}