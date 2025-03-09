package org.chorus.item

import cn.nukkit.block.*

class ItemFence @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.FENCE, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "Oak Fence"
                blockUnsafe = Block.get(BlockID.OAK_FENCE)
                return
            }

            1 -> {
                this.name = "Spruce Fence"
                blockUnsafe = Block.get(BlockID.SPRUCE_FENCE)
                this.meta = 0
                return
            }

            2 -> {
                this.name = "Birch Fence"
                blockUnsafe = Block.get(BlockID.BIRCH_FENCE)
                this.meta = 0
                return
            }

            3 -> {
                this.name = "Jungle Fence"
                blockUnsafe = Block.get(BlockID.JUNGLE_FENCE)
                this.meta = 0
                return
            }

            4 -> {
                this.name = "Acacia Fence"
                blockUnsafe = Block.get(BlockID.ACACIA_FENCE)
                this.meta = 0
                return
            }

            5 -> {
                this.name = "Dark Oak Fence"
                blockUnsafe = Block.get(BlockID.DARK_OAK_FENCE)
                this.meta = 0
            }
        }
    }
}