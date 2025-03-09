package org.chorus.item

import cn.nukkit.block.BlockBed
import cn.nukkit.block.BlockID
import cn.nukkit.utils.DyeColor

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemBed @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(BlockID.BED, meta, count) {
    override fun internalAdjust() {
        name = DyeColor.getByWoolData(meta).getName() + " Bed"
        block = BlockBed.properties.defaultState.toBlock()
    }

    override val maxStackSize: Int
        get() = 1
}
