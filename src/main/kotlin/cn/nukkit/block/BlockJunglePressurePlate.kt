package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockJunglePressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenPressurePlate(blockstate) {
    override val name: String
        get() = "Jungle Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}