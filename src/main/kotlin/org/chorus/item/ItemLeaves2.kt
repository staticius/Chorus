package org.chorus.item

import org.chorus.block.*

class ItemLeaves2 @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.LEAVES2, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                name = "Acacia Leaves"
                blockUnsafe = Block.get(BlockID.ACACIA_LEAVES)
                this.meta = 0
            }

            1 -> {
                name = "Dark Oak Leaves"
                blockUnsafe = Block.get(BlockID.DARK_OAK_LEAVES)
                this.meta = 0
            }

            else -> throw IllegalArgumentException("Invalid damage: $damage")
        }
    }
}