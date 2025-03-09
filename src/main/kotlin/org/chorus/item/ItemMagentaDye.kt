package org.chorus.item

import cn.nukkit.utils.DyeColor

class ItemMagentaDye : ItemDye(ItemID.Companion.MAGENTA_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.MAGENTA

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}