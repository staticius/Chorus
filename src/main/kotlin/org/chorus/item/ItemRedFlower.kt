package org.chorus.item

import org.chorus.block.*

class ItemRedFlower : Item(ItemID.Companion.RED_FLOWER) {
    init {
        this.block = Block.get(BlockID.POPPY)
    }
}