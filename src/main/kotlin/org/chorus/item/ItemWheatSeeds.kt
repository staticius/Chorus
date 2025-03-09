package org.chorus.item

import cn.nukkit.block.*

class ItemWheatSeeds @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.WHEAT_SEEDS, 0, count, "Wheat Seeds") {
    init {
        this.block = Block.get(BlockID.WHEAT)
    }
}