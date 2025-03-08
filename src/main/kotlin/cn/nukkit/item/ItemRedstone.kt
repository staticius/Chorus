package cn.nukkit.item

import cn.nukkit.block.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemRedstone @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.REDSTONE, meta, count, "Redstone Dust") {
    init {
        this.block = Block.get(BlockID.REDSTONE_WIRE)
    }
}
