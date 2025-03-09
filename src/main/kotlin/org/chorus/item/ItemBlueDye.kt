package org.chorus.item

import org.chorus.utils.DyeColor

class ItemBlueDye : ItemDye(ItemID.Companion.BLUE_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLUE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}