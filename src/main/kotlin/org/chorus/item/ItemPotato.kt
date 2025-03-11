package org.chorus.item

import org.chorus.block.*


open class ItemPotato @JvmOverloads constructor(
    id: String = ItemID.Companion.POTATO,
    meta: Int = 0,
    count: Int = 1,
    name: String? = "Potato"
) :
    ItemFood(id, meta, count, name) {
    constructor(meta: Int) : this(ItemID.Companion.POTATO, meta, 1, "Potato")

    init {
        this.block = Block.get(BlockID.POTATOES)
    }

    override val foodRestore: Int
        get() = 1

    override val saturationRestore: Float
        get() = 0.6f
}
