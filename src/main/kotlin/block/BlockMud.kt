package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.item.ItemTool

class BlockMud : BlockSolid, Natural {
    constructor() : super(properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Mud"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 0.5

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MUD)
    }
}
