package org.chorus.item

import cn.nukkit.block.*

class ItemPlanks @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.PLANKS, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "Oak Planks"
                blockUnsafe = Block.get(BlockID.OAK_PLANKS)
                this.name = "Spruce Planks"
                blockUnsafe = Block.get(BlockID.SPRUCE_PLANKS)
                this.name = "Birch Planks"
                blockUnsafe = Block.get(BlockID.BIRCH_PLANKS)
                this.name = "Jungle Planks"
                blockUnsafe = Block.get(BlockID.JUNGLE_PLANKS)
                this.name = "Acacia Planks"
                blockUnsafe = Block.get(BlockID.ACACIA_PLANKS)
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }

            1 -> {
                this.name = "Spruce Planks"
                blockUnsafe = Block.get(BlockID.SPRUCE_PLANKS)
                this.name = "Birch Planks"
                blockUnsafe = Block.get(BlockID.BIRCH_PLANKS)
                this.name = "Jungle Planks"
                blockUnsafe = Block.get(BlockID.JUNGLE_PLANKS)
                this.name = "Acacia Planks"
                blockUnsafe = Block.get(BlockID.ACACIA_PLANKS)
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }

            2 -> {
                this.name = "Birch Planks"
                blockUnsafe = Block.get(BlockID.BIRCH_PLANKS)
                this.name = "Jungle Planks"
                blockUnsafe = Block.get(BlockID.JUNGLE_PLANKS)
                this.name = "Acacia Planks"
                blockUnsafe = Block.get(BlockID.ACACIA_PLANKS)
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }

            3 -> {
                this.name = "Jungle Planks"
                blockUnsafe = Block.get(BlockID.JUNGLE_PLANKS)
                this.name = "Acacia Planks"
                blockUnsafe = Block.get(BlockID.ACACIA_PLANKS)
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }

            4 -> {
                this.name = "Acacia Planks"
                blockUnsafe = Block.get(BlockID.ACACIA_PLANKS)
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }

            5 -> {
                this.name = "Dark Oak Planks"
                blockUnsafe = Block.get(BlockID.DARK_OAK_PLANKS)
            }
        }
        this.meta = 0
    }
}