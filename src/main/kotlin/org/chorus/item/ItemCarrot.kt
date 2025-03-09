package org.chorus.item

import org.chorus.block.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemCarrot @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.CARROT, 0, count, "Carrot") {
    init {
        this.block = Block.get(BlockID.CARROTS)
    }

    override val foodRestore: Int
        get() = 3

    override val saturationRestore: Float
        get() = 4.8f
}
