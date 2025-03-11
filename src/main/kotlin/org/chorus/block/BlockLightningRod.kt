package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.*
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.utils.Faceable

class BlockLightningRod : BlockTransparent, Faceable {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "LightningRod"

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun canBeFlowedInto(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 6.0

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
        this.blockFace = face
        level.setBlock(block.position, this, true, true)
        return true
    }

    override var blockFace: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            this.setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.FACING_DIRECTION,
                face!!.index
            )
        }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHTNING_ROD, CommonBlockProperties.FACING_DIRECTION)

    }
}
