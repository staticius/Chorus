package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenDoor(blockstate) {
    override val name: String
        get() = "Pale Oak Door Block"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.PALE_OAK_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}