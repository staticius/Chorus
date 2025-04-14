package org.chorus.item

import org.chorus.block.BlockWheat

class ItemWheatSeeds @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.WHEAT_SEEDS, 0, count, "Wheat Seeds") {
    init {
        this.blockState = BlockWheat.properties.defaultState
    }
}