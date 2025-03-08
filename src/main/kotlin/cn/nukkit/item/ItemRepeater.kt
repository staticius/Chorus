package cn.nukkit.item

import cn.nukkit.block.*

class ItemRepeater : Item(ItemID.Companion.REPEATER, 0, 1, "Redstone Repeater") {
    init {
        this.block = Block.get(BlockID.UNPOWERED_REPEATER)
    }
}