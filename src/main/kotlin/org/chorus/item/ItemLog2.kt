package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemLog2 @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.LOG2, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "Acacia Log"
                this.block = Block.get(BlockID.ACACIA_LOG)
                this.name = "Dark Oak Log"
                this.block = Block.get(BlockID.DARK_OAK_LOG)
            }

            1 -> {
                this.name = "Dark Oak Log"
                this.block = Block.get(BlockID.DARK_OAK_LOG)
            }
        }
        this.meta = 0
    }


    override fun equalItemBlock(item: Item): Boolean {
        if (this.isBlock() && item.isBlock()) {
            return this.blockUnsafe!!.properties == item.blockUnsafe!!.properties
        }
        return true
    }
}