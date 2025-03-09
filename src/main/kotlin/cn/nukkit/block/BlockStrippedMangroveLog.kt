package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedMangroveLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.PROPERTIES.getDefaultState()
    }

    override fun getWoodType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_MANGROVE_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}