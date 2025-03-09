package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace

class BlockBoneBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val name: String
        get() = "Bone Block"

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
        this.pillarAxis = face.axis
        level.setBlock(block.position, this, true)
        return true
    }

    var pillarAxis: BlockFace.Axis?
        get() = getPropertyValue(
            CommonBlockProperties.PILLAR_AXIS
        )
        set(axis) {
            setPropertyValue(
                CommonBlockProperties.PILLAR_AXIS,
                axis
            )
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BONE_BLOCK, CommonBlockProperties.DEPRECATED, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}