package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedMangroveWood @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override val name: String
        get() = "Stripped Mangrove Wood"

    override fun getWoodType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_MANGROVE_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}