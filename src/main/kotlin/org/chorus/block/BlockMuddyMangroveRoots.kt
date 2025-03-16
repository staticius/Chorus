package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace

class BlockMuddyMangroveRoots : BlockSolid, Natural {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Muddy Mangrove Roots"

    override val hardness: Double
        get() = 0.7

    override val resistance: Double
        get() = 0.7

    override val toolTier: Int
        get() = ItemTool.TYPE_SHOVEL

    override val isTransparent: Boolean
        get() = true

    var pillarAxis: BlockFace.Axis
        get() = getPropertyValue(
            CommonBlockProperties.PILLAR_AXIS
        )
        set(axis) {
            setPropertyValue(
                CommonBlockProperties.PILLAR_AXIS,
                axis!!
            )
        }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        pillarAxis = face.axis
        level.setBlock(block.position, this, true, true)
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MUDDY_MANGROVE_ROOTS, CommonBlockProperties.PILLAR_AXIS)

    }
}
