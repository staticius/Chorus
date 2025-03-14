package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Pale Oak Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}