package org.chorus.item

import org.chorus.block.*


class ItemSugarCane @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.SUGAR_CANE, 0, count) {
    init {
        this.block = Block.get(BlockID.REEDS)
    }
}
