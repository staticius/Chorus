package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWaxedOxidizedCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperTrapdoor(blockstate) {
    override val name: String
        get() = "Waxed Oxidized Copper Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_OXIDIZED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}