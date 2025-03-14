package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemLog @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.LOG, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "Oak Log"
                blockUnsafe = Block.get(BlockID.OAK_LOG)
                this.name = "Spruce Log"
                blockUnsafe = Block.get(BlockID.SPRUCE_LOG)
                this.name = "Birch Log"
                blockUnsafe = Block.get(BlockID.BIRCH_LOG)
                this.name = "Jungle Log"
                blockUnsafe = Block.get(BlockID.JUNGLE_LOG)
            }

            1 -> {
                this.name = "Spruce Log"
                blockUnsafe = Block.get(BlockID.SPRUCE_LOG)
                this.name = "Birch Log"
                blockUnsafe = Block.get(BlockID.BIRCH_LOG)
                this.name = "Jungle Log"
                blockUnsafe = Block.get(BlockID.JUNGLE_LOG)
            }

            2 -> {
                this.name = "Birch Log"
                blockUnsafe = Block.get(BlockID.BIRCH_LOG)
                this.name = "Jungle Log"
                blockUnsafe = Block.get(BlockID.JUNGLE_LOG)
            }

            3 -> {
                this.name = "Jungle Log"
                blockUnsafe = Block.get(BlockID.JUNGLE_LOG)
            }
        }
        this.meta = 0
    }


    override fun equalItemBlock(item: Item): Boolean {
        if (this.isBlock && item.isBlock) {
            return this.blockUnsafe.properties == item.blockUnsafe.properties
        }
        return true
    }
}