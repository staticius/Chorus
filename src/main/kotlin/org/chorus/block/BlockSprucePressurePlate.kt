package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockSprucePressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Spruce Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}