package org.chorus.item

import cn.nukkit.block.Block

abstract class ItemHangingSign(id: String) : Item(id) {
    init {
        this.block = Block.get(id)
    }

    override val maxStackSize: Int
        get() = 16
}
