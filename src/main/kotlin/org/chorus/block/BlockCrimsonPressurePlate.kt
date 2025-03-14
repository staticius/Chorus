package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCrimsonPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Crimson Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CRIMSON_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}