package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedPaleOakLog @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.PALE_OAK
    }

    override fun getStrippedState(): BlockState {
        return Companion.properties.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_PALE_OAK_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}