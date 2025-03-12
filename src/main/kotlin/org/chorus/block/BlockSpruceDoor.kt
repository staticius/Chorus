package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodenDoor(blockstate) {
    override val name: String
        get() = "Spruce Door Block"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.SPRUCE_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}