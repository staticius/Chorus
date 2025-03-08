package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemGreenDye : ItemDye(ItemID.Companion.GREEN_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}