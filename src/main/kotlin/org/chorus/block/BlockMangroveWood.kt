package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockMangroveWood @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWood(blockstate) {
    override val name: String
        get() = "Mangrove Wood"

    override fun getWoodType(): WoodType {
        throw UnsupportedOperationException()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MANGROVE_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}