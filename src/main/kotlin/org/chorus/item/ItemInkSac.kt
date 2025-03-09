package org.chorus.item

import org.chorus.utils.DyeColor

class ItemInkSac : ItemDye(ItemID.Companion.INK_SAC) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLACK

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}