package org.chorus.item

import org.chorus.block.*

class ItemRepeater : Item(ItemID.Companion.REPEATER, 0, 1, "Redstone Repeater") {
    init {
        this.block = Block.get(BlockID.UNPOWERED_REPEATER)
    }
}