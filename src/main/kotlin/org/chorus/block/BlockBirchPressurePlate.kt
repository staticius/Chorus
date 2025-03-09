package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Birch Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(BIRCH_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}