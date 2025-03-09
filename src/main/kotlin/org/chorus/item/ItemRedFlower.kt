package org.chorus.item

import cn.nukkit.block.*

class ItemRedFlower : Item(ItemID.Companion.RED_FLOWER) {
    init {
        this.block = Block.get(BlockID.POPPY)
    }
}