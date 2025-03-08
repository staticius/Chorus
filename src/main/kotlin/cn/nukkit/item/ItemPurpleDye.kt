package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemPurpleDye : ItemDye(ItemID.Companion.PURPLE_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.PURPLE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}