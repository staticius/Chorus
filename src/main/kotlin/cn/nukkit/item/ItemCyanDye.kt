package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemCyanDye : ItemDye(ItemID.Companion.CYAN_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.CYAN
}