package org.chorus_oss.chorus.item

import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.block.BlockBeetroot
import org.chorus_oss.chorus.block.BlockID

class ItemBeetrootSeeds : Item(ItemID.BEETROOT_SEEDS) {
    init {
        this.blockState = BlockBeetroot.properties.defaultState
    }
}