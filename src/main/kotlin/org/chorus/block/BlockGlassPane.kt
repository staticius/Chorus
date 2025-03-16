package org.chorus.block

import org.chorus.item.Item

open class BlockGlassPane : BlockThin {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    override val name: String
        get() = "Glass Pane"

    override val resistance: Double
        get() = 1.5

    override val waterloggingLevel: Int
        get() = 1

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
        val properties: BlockProperties = BlockProperties(BlockID.GLASS_PANE)
    }
}
