package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockMangroveDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenDoor(blockstate) {
    override val name: String
        get() = "Mangrove Door Block"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )
            get() = Companion.field
    }
}