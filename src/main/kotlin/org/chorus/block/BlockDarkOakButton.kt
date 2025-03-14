package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Dark Oak Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.DARK_OAK_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}