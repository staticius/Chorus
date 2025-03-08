package cn.nukkit.item

import cn.nukkit.block.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemSugarCane @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.SUGAR_CANE, 0, count) {
    init {
        this.block = Block.get(BlockID.REEDS)
    }
}
