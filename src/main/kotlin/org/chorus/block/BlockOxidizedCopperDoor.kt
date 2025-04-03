package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

class BlockOxidizedCopperDoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperDoorBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.OXIDIZED_COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )
    }
}