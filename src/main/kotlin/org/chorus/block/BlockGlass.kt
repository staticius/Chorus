package org.chorus.block

import cn.nukkit.item.*

/**
 * @author Angelic47 (Nukkit Project)
 */
open class BlockGlass : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Glass"

    override val resistance: Double
        get() = 0.3

    override val hardness: Double
        get() = 0.3

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(GLASS)
            get() = Companion.field
    }
}
