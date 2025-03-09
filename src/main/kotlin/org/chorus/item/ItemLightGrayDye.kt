package org.chorus.item

import org.chorus.utils.DyeColor

class ItemLightGrayDye : ItemDye(ItemID.Companion.LIGHT_GRAY_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIGHT_GRAY

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}