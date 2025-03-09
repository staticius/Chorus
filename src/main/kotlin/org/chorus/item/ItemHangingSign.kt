package org.chorus.item

import org.chorus.block.Block

abstract class ItemHangingSign(id: String) : Item(id) {
    init {
        this.block = Block.get(id)
    }

    override val maxStackSize: Int
        get() = 16
}
