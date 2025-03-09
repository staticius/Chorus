package org.chorus.item

import cn.nukkit.utils.DyeColor

class ItemBrownDye : ItemDye(ItemID.Companion.BROWN_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.BROWN

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}