package org.chorus.block

import org.chorus.item.Item

open class BlockGlass : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Glass"

    override val resistance: Double
        get() = 0.3

    override val hardness: Double
        get() = 0.3

    override fun getDrops(item: Item): Array<Item> {
        return Item.EMPTY_ARRAY
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GLASS)
    }
}
