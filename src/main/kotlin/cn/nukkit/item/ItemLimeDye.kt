package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemLimeDye : ItemDye(ItemID.Companion.LIME_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIME

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}