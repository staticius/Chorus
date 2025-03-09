package org.chorus.item

import cn.nukkit.block.*

class ItemTorchflowerSeeds @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.TORCHFLOWER_SEEDS, meta, count, "Torchflower Seeds") {
    init {
        this.block = Block.get(BlockID.TORCHFLOWER_CROP)
    }
}
