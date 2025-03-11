package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.level.Sound

class BlockCherryTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Cherry Trapdoor"

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_CHERRY_WOOD_TRAPDOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_CHERRY_WOOD_TRAPDOOR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.CHERRY_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}