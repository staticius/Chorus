package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockBambooPressurePlate(blockState: BlockState?) : BlockWoodenPressurePlate(blockState) {
    override val name: String
        get() = "Bamboo Pressure Plate"

    companion object {
        val properties: BlockProperties = BlockProperties(BAMBOO_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}