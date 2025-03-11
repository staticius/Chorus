package org.chorus.block

import org.chorus.item.ItemTool

class BlockShroomlight : BlockTransparent {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Shroomlight"

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override val resistance: Double
        get() = 1.0

    override val hardness: Double
        get() = 1.0

    override val lightLevel: Int
        get() = 15

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SHROOMLIGHT)

    }
}
