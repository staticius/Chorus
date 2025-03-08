package cn.nukkit.item

import cn.nukkit.block.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemString @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.STRING, meta, count, "String") {
    init {
        this.block = Block.get(BlockID.TRIP_WIRE)
    }
}
