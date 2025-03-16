package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.DoublePlantType

//todo complete
class BlockPitcherPlant @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDoublePlant(blockstate) {
    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.PITCHER_PLANT

    override val name: String
        get() = "Pitcher Plant"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PITCHER_PLANT, CommonBlockProperties.UPPER_BLOCK_BIT)

    }
}