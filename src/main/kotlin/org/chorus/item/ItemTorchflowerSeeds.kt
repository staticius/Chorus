package org.chorus.item

import org.chorus.block.BlockTorchflowerCrop

class ItemTorchflowerSeeds @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.TORCHFLOWER_SEEDS, meta, count, "Torchflower Seeds") {
    init {
        this.blockState = BlockTorchflowerCrop.properties.defaultState
    }
}
