package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.block.BlockPumpkin
import org.chorus.block.BlockPumpkinStem

class ItemPumpkinSeeds @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.PUMPKIN_SEEDS, 0, count, "Pumpkin Seeds") {
    init {
        this.blockState = BlockPumpkinStem.properties.defaultState
    }
}