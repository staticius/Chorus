package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWarpedPressurePlate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Warped Pressure Plate"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}