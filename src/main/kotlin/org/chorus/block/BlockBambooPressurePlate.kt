package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockBambooPressurePlate(blockState: BlockState?) : BlockWoodenPressurePlate(blockState) {
    override val name: String
        get() = "Bamboo Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BAMBOO_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}