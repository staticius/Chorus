package org.chorus.item

import cn.nukkit.block.*

class ItemComparator @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.COMPARATOR, meta, count, "Redstone Comparator") {
    init {
        this.block = Block.get(BlockID.UNPOWERED_COMPARATOR)
    }
}