package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

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
            COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )
            get() = Companion.field
    }
}