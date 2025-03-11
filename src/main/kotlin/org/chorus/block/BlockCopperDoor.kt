package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCopperDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperDoorBase(blockstate) {
    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 15.0

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}