package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBirchPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Birch Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BIRCH_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            
    }
}