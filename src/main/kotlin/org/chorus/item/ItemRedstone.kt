package org.chorus.item

import org.chorus.block.*


class ItemRedstone @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.REDSTONE, meta, count, "Redstone Dust") {
    init {
        this.block = Block.get(BlockID.REDSTONE_WIRE)
    }
}
