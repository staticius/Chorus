package org.chorus.item

import cn.nukkit.utils.DyeColor

class ItemBlueDye : ItemDye(ItemID.Companion.BLUE_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLUE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}