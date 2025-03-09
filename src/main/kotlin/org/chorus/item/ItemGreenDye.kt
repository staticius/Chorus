package org.chorus.item

import org.chorus.utils.DyeColor

class ItemGreenDye : ItemDye(ItemID.Companion.GREEN_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}