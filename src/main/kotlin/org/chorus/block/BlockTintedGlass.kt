package org.chorus.block

import org.chorus.item.*

class BlockTintedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockGlass(blockstate) {
    override val name: String
        get() = "Tinted Glass"

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(toItem())
    }

    override fun canSilkTouch(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TINTED_GLASS)

    }
}