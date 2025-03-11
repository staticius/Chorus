package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Dark Oak Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DARK_OAK_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}