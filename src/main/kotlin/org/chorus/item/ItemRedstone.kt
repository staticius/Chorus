package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.block.BlockRedstoneWire


class ItemRedstone @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.REDSTONE, meta, count, "Redstone Dust") {
    init {
        this.blockState = BlockRedstoneWire.properties.defaultState
    }
}
