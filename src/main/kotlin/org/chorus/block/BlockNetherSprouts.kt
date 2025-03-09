package org.chorus.block

import org.chorus.item.Item

class BlockNetherSprouts @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHanging(blockstate) {
    override val name: String
        get() = "Nether Sprouts Block"

    override fun getDrops(item: Item): Array<Item?>? {
        if (item.isShears) {
            return arrayOf(toItem())
        }
        return Item.EMPTY_ARRAY
    }

    override val burnChance: Int
        get() = 5

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_SPROUTS)
            get() = Companion.field
    }
}