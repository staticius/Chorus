package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemLeaves @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.LEAVES, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                name = "Oak Leaves"
                blockUnsafe = Block.get(BlockID.OAK_LEAVES)
            }

            1 -> {
                name = "Spruce Leaves"
                blockUnsafe = Block.get(BlockID.SPRUCE_LEAVES)
                this.meta = 0
            }

            2 -> {
                name = "Birch Leaves"
                blockUnsafe = Block.get(BlockID.BIRCH_LEAVES)
                this.meta = 0
            }

            3 -> {
                name = "Jungle Leaves"
                blockUnsafe = Block.get(BlockID.JUNGLE_LEAVES)
                this.meta = 0
            }

            else -> throw IllegalArgumentException("Invalid damage: $damage")
        }
    }
}