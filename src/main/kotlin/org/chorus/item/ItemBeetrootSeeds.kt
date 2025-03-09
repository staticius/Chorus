package org.chorus.item

import cn.nukkit.block.*

class ItemBeetrootSeeds : Item(ItemID.Companion.BEETROOT_SEEDS) {
    init {
        this.block = Block.get(BlockID.BEETROOT)
    }
}