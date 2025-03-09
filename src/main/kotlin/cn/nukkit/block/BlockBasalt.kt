package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace

open class BlockBasalt @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Basalt"

    override val hardness: Double
        get() = 1.25

    override val resistance: Double
        get() = 4.2

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    open var pillarAxis: BlockFace.Axis?
        get() = getPropertyValue(
            CommonBlockProperties.PILLAR_AXIS
        )
        set(axis) {
            setPropertyValue(
                CommonBlockProperties.PILLAR_AXIS,
                axis
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
        val properties: BlockProperties = BlockProperties(BASALT, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
