package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedCherryLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override val name: String
        get() = "Stripped Cherry Log"

    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.Companion.PROPERTIES.getDefaultState()
    }

    override fun getWoodType(): WoodType {
        throw UnsupportedOperationException()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_CHERRY_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}