package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

open class BlockWoodenButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockButton(blockstate) {
    override val name: String
        get() = "Oak Button"

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WOODEN_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}