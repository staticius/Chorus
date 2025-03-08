package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemLapisLazuli : ItemDye(ItemID.Companion.LAPIS_LAZULI) {
    override val dyeColor: DyeColor
        get() = DyeColor.BLUE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}