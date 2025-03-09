package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPolishedBlackstonePressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStonePressurePlate(blockstate) {
    override val name: String
        get() = "Polished Blackstone Pressure Plate"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_BLACKSTONE_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}