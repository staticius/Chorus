package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceButton @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Spruce Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SPRUCE_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}