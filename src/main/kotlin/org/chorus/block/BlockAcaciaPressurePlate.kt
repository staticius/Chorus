package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Acacia Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}