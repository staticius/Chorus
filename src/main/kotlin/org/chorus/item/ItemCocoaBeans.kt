package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockCocoa
import org.chorus.block.BlockID
import org.chorus.utils.DyeColor

class ItemCocoaBeans : ItemDye(ItemID.COCOA_BEANS) {
    init {
        this.blockState = BlockCocoa.properties.defaultState
    }

    override val dyeColor: DyeColor
        get() = DyeColor.BROWN

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}