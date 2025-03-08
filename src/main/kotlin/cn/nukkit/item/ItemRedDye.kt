package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemRedDye : ItemDye(ItemID.Companion.RED_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.RED

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}