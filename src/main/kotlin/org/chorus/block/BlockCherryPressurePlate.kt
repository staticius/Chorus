package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCherryPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Cherry Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHERRY_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}