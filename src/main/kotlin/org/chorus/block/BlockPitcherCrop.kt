package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

//todo complete
class BlockPitcherCrop @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCrops(blockstate) {
    override val name: String
        get() = "Pitcher Crop"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PITCHER_CROP, CommonBlockProperties.GROWTH, CommonBlockProperties.UPPER_BLOCK_BIT)

    }
}