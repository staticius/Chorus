package org.chorus.item

import org.chorus.utils.DyeColor

class ItemWhiteDye : ItemDye(ItemID.Companion.WHITE_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.WHITE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}