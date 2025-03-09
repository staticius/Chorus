package org.chorus.item

import org.chorus.block.*

class ItemPumpkinSeeds @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.PUMPKIN_SEEDS, 0, count, "Pumpkin Seeds") {
    init {
        this.block = Block.get(BlockID.PUMPKIN_STEM)
    }
}