package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.blockentity.*
import org.chorus.item.*
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromHorizontalIndex
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.Tag
import org.chorus.utils.Faceable
import java.util.*

class BlockDecoratedPot : BlockFlowable, Faceable, BlockEntityHolder<BlockEntityDecoratedPot?> {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Decorated Pot"

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
        val nbt = CompoundTag()

        nbt.putString("id", BlockEntity.DECORATED_POT)
        nbt.putByte("isMovable", 1)

        if (item.namedTag != null) {
            val customData: Map<String?, Tag?> = item.namedTag!!.getTags()
            for ((key, value) in customData) {
                nbt.put(key, value)
            }
        }

        nbt.putInt("x", position.x.toInt())
        nbt.putInt("y", position.y.toInt())
        nbt.putInt("z", position.y.toInt())

        this.blockFace = player!!.getDirection()!!.getOpposite()
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
    }

    override fun getBlockEntityClass(): Class<out BlockEntityDecoratedPot> {
        return BlockEntityDecoratedPot::class.java
    }

    override fun getBlockEntityType(): String {
        return BlockEntity.DECORATED_POT
    }

    override var blockFace: BlockFace
        get() = fromHorizontalIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.DIRECTION,
                Objects.requireNonNullElse(face, BlockFace.SOUTH).horizontalIndex
            )
        }

    override fun canPassThrough(): Boolean {
        return false
    }

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override fun recalculateBoundingBox(): AxisAlignedBB {
        return this
    }

    override val waterloggingLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DECORATED_POT, CommonBlockProperties.DIRECTION)

    }
}