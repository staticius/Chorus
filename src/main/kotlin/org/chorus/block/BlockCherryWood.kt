package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockCherryWood @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWood(blockstate) {
    override val name: String
        get() = "Cherry Wood"

    override fun getWoodType(): WoodType {
        return WoodType.CHERRY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHERRY_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}