package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockAcaciaWood(blockstate: BlockState) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ACACIA_WOOD, CommonBlockProperties.PILLAR_AXIS)
    }
}