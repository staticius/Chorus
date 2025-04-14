package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockBeetroot
import org.chorus.block.BlockID

class ItemBeetrootSeeds : Item(ItemID.BEETROOT_SEEDS) {
    init {
        this.blockState = BlockBeetroot.properties.defaultState
    }
}