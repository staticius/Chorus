package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace


class BlockChain : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Chain"

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
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override val hardness: Double
        get() = 5.0

    override val waterloggingLevel: Int
        get() = 1

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override var minX: Double
        get() = position.x + 7 / 16.0
        set(minX) {
            super.minX = minX
        }

    override var maxX: Double
        get() = position.x + 9 / 16.0
        set(maxX) {
            super.maxX = maxX
        }

    override var minZ: Double
        get() = position.z + 7 / 16.0
        set(minZ) {
            super.minZ = minZ
        }

    override var maxZ: Double
        get() = position.z + 9 / 16.0
        set(maxZ) {
            super.maxZ = maxZ
        }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHAIN, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
