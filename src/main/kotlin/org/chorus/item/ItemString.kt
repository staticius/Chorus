package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID


class ItemString @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.STRING, meta, count, "String") {
    init {
        this.block = Block.get(BlockID.TRIP_WIRE)
    }
}
