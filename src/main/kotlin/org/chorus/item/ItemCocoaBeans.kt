package org.chorus.item

import cn.nukkit.block.*
import cn.nukkit.utils.DyeColor

class ItemCocoaBeans : ItemDye(ItemID.Companion.COCOA_BEANS) {
    init {
        this.block = Block.get(BlockID.COCOA)
    }

    override val dyeColor: DyeColor
        get() = DyeColor.BROWN

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}