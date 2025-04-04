package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedCopperDoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperDoorBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.UNAFFECTED


    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}