package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.blockentity.*
import cn.nukkit.item.*
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromHorizontalIndex
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.Tag
import cn.nukkit.utils.Faceable
import java.util.*

class BlockDecoratedPot : BlockFlowable, Faceable, BlockEntityHolder<BlockEntityDecoratedPot?> {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

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

    override var blockFace: BlockFace?
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

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        return this
    }

    override val waterloggingLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(DECORATED_POT, CommonBlockProperties.DIRECTION)
            get() = Companion.field
    }
}