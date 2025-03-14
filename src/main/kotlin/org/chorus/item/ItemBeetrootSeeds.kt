package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemBeetrootSeeds : Item(ItemID.Companion.BEETROOT_SEEDS) {
    init {
        this.block = Block.get(BlockID.BEETROOT)
    }
}