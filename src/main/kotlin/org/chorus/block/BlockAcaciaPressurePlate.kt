package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Acacia Pressure Plate"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ACACIA_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}