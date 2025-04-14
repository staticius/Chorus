package org.chorus.item

import org.chorus.block.BlockState

abstract class ItemHangingSign(blockState: BlockState) : Item(blockState.identifier) {
    init {
        this.blockState = blockState
    }

    override val maxStackSize: Int
        get() = 16
}
