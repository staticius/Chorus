package org.chorus.item

import org.chorus.utils.DyeColor

class ItemLapisLazuli : ItemDye(ItemID.Companion.LAPIS_LAZULI) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLUE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}