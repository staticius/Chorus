package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Acacia Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            ACACIA_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}