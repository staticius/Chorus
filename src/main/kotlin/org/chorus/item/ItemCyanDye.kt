package org.chorus.item

import org.chorus.utils.DyeColor

class ItemCyanDye : ItemDye(ItemID.Companion.CYAN_DYE) {
    override val dyeColor: DyeColor
        get() = DyeColor.CYAN
}