package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.level.Sound

class BlockCherryDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenDoor(blockstate) {
    override val name: String
        get() = "Cherry Door Block"

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_CHERRY_WOOD_DOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_CHERRY_WOOD_DOOR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.CHERRY_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}