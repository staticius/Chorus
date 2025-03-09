package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCrimsonButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Crimson Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            CRIMSON_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}