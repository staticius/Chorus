package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.utils.random.NukkitRandom
import java.util.*

abstract class BlockLichen(blockState: BlockState?) : BlockTransparent(blockState) {
    val growthSides: Array<BlockFace>
        get() {
            val returns =
                Arrays.stream(BlockFace.entries.toTypedArray()).filter { side: BlockFace -> this.isGrowthToSide(side) }
            return returns.toArray<BlockFace> { _Dummy_.__Array__() }
        }

    open fun witherAtSide(side: BlockFace) {
        if (isGrowthToSide(side)) {
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.MULTI_FACE_DIRECTION_BITS,
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) xor (1 shl side.dUSWNEIndex)
            )
            level.setBlock(this.position, this, true, true)
        }
    }

    fun isGrowthToSide(side: BlockFace): Boolean {
        return ((getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) shr side.dUSWNEIndex) and 0x1) > 0
    }

    fun growToSide(side: BlockFace) {
        if (!isGrowthToSide(side)) {
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.MULTI_FACE_DIRECTION_BITS,
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) or (1 shl side.dUSWNEIndex)
            )
            level.setBlock(this.position, this, true, true)
        }
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
        if (!target.isSolid && target is BlockLichen) {
            return false
        }

        var currentMeta = 0
        if (block is BlockLichen) {
            currentMeta = block.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS)
        }

        setPropertyValue<Int, IntPropertyType>(
            CommonBlockProperties.MULTI_FACE_DIRECTION_BITS,
            currentMeta or (1 shl face.getOpposite()!!.dUSWNEIndex)
        )

        if (getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) == currentMeta) {
            val sides = BlockFace.entries.toTypedArray()
            val faceStream = Arrays.stream(sides).filter { side: BlockFace ->
                block.getSide(side)!!
                    .isSolid(side) && !isGrowthToSide(side)
            }
            val optionalFace = faceStream.findFirst()
            if (optionalFace.isPresent) {
                growToSide(optionalFace.get())
                return true
            }

            return false
        }

        level.setBlock(block.position, this, true, true)
        return true
    }

    override fun onUpdate(type: Int): Int {
        for (side in BlockFace.entries) {
            val support = this.getSide(side)
            if (isGrowthToSide(side) && support != null && !support.isSolid) {
                this.witherAtSide(side)
            }
        }
        return super.onUpdate(type)
    }

    override val hardness: Double
        get() = 0.2

    override val resistance: Double
        get() = 1.0

    override fun canPassThrough(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun canSilkTouch(): Boolean {
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvest(item: Item): Boolean {
        return item.isAxe || item.isShears
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun canBeReplaced(): Boolean {
        return true
    }

    override fun canBeFlowedInto(): Boolean {
        return true
    }

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun toItem(): Item? {
        return ItemBlock(properties.defaultState.toBlock())
    }

    companion object {
        val RANDOM: NukkitRandom = NukkitRandom()
    }
}
