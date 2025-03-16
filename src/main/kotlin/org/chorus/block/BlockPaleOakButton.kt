package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakButton @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Pale Oak Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.PALE_OAK_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}