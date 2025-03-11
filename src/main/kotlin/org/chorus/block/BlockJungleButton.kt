package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Jungle Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.Companion.JUNGLE_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}