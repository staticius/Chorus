package org.chorus.item

import org.chorus.block.BlockBed
import org.chorus.block.BlockID
import org.chorus.utils.DyeColor


class ItemBed @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(BlockID.BED, meta, count) {
    override fun internalAdjust() {
        name = DyeColor.getByWoolData(meta).name + " Bed"
        block = BlockBed.properties.defaultState.toBlock()
    }

    override val maxStackSize: Int
        get() = 1
}
