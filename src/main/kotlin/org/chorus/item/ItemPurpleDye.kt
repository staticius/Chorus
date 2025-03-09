package org.chorus.item

import org.chorus.utils.DyeColor

class ItemPurpleDye : ItemDye(ItemID.Companion.PURPLE_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.PURPLE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}