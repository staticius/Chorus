package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.block.BlockPoppy

class ItemRedFlower : Item(ItemID.Companion.RED_FLOWER) {
    init {
        this.blockState = BlockPoppy.properties.defaultState
    }
}