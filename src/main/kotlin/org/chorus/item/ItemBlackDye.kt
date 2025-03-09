package org.chorus.item

import cn.nukkit.utils.DyeColor

class ItemBlackDye : ItemDye(ItemID.Companion.BLACK_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLACK

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}