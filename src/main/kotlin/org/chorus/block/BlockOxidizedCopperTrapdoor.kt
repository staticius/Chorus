package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOxidizedCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperTrapdoor(blockstate) {
    override val name: String
        get() = "Oxidized Copper Trapdoor"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.OXIDIZED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}