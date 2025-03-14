package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemRedFlower : Item(ItemID.Companion.RED_FLOWER) {
    init {
        this.block = Block.get(BlockID.POPPY)
    }
}