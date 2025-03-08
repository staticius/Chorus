package cn.nukkit.item

import cn.nukkit.block.BlockSweetBerryBush

class ItemSweetBerries @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.SWEET_BERRIES, meta, count, "Sweet Berries") {
    init {
        this.block = BlockSweetBerryBush()
    }

    override val foodRestore: Int
        get() = 2

    override val saturationRestore: Float
        get() = 0.4f
}
