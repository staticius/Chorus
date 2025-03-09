package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockAcaciaWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}