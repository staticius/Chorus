package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockBirchWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.BIRCH
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BIRCH_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}