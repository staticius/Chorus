package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemWheatSeeds @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.WHEAT_SEEDS, 0, count, "Wheat Seeds") {
    init {
        this.block = Block.get(BlockID.WHEAT)
    }
}