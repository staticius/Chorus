package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWarpedButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Warped Button"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WARPED_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}