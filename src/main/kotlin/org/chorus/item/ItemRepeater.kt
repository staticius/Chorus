package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.block.BlockUnpoweredComparator
import org.chorus.block.BlockUnpoweredRepeater

class ItemRepeater : Item(ItemID.Companion.REPEATER, 0, 1, "Redstone Repeater") {
    init {
        this.blockState = BlockUnpoweredRepeater.properties.defaultState
    }
}