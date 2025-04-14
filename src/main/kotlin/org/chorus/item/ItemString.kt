package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.block.BlockTripWire


class ItemString @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.STRING, meta, count, "String") {
    init {
        this.blockState = BlockTripWire.properties.defaultState
    }
}
