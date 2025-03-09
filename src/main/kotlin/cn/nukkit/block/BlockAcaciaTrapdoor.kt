package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockAcaciaTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Acacia Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            ACACIA_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}