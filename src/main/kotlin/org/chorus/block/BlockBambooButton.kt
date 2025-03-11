package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBambooButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Bamboo Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BAMBOO_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}