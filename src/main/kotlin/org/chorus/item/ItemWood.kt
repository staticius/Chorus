package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemWood @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.WOOD, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0, 6, 7, 14, 15 -> {
                name = "Oak Wood"
                blockUnsafe = Block.get(BlockID.OAK_WOOD)
            }

            1 -> {
                name = "Spruce Wood"
                blockUnsafe = Block.get(BlockID.SPRUCE_WOOD)
            }

            2 -> {
                name = "Birch Wood"
                blockUnsafe = Block.get(BlockID.BIRCH_WOOD)
            }

            3 -> {
                name = "Jungle Wood"
                blockUnsafe = Block.get(BlockID.JUNGLE_WOOD)
            }

            4 -> {
                name = "Acacia Wood"
                blockUnsafe = Block.get(BlockID.ACACIA_WOOD)
            }

            5 -> {
                name = "Dark Oak Wood"
                blockUnsafe = Block.get(BlockID.DARK_OAK_WOOD)
            }

            8 -> {
                name = "Stripped Oak Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_OAK_WOOD)
            }

            9 -> {
                name = "Stripped Spruce Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_SPRUCE_WOOD)
            }

            10 -> {
                name = "Stripped Birch Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_BIRCH_WOOD)
            }

            11 -> {
                name = "Stripped Jungle Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_JUNGLE_WOOD)
            }

            12 -> {
                name = "Stripped Acacia Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_ACACIA_WOOD)
            }

            13 -> {
                name = "Stripped Dark Oak Wood"
                blockUnsafe = Block.get(BlockID.STRIPPED_DARK_OAK_WOOD)
            }

            else -> throw IllegalArgumentException("Invalid damage: $damage")
        }
        this.meta = 0
    }

    override fun equalItemBlock(item: Item): Boolean {
        if (this.isBlock() && item.isBlock()) {
            return this.getSafeBlockState() == item.getSafeBlockState()
        }
        return true
    }
}