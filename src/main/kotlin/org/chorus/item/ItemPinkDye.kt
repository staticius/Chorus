package org.chorus.item

import cn.nukkit.utils.DyeColor

class ItemPinkDye : ItemDye(ItemID.Companion.PINK_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.PINK

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}