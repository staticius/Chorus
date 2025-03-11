package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWeatheredCopperDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperDoorBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.WEATHERED_COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}