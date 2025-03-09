package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCherryButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Cherry Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            CHERRY_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}