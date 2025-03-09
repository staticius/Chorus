package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDarkOakPressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Dark Oak Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(DARK_OAK_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}