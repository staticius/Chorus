package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntitySkull
import org.chorus.event.redstone.RedstoneUpdateEvent
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.math.SimpleAxisAlignedBB
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.Faceable
import org.chorus.utils.RedstoneComponent
import kotlin.math.floor

abstract class BlockHead(blockState: BlockState) : BlockTransparent(blockState), RedstoneComponent,
    BlockEntityHolder<BlockEntitySkull?>, Faceable {
    override val blockEntityType: String
        get() = BlockEntity.SKULL

    override val blockEntityClass: Class<out BlockEntitySkull>
        get() = BlockEntitySkull::class.java

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 5.0

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
        if (face == BlockFace.DOWN) {
            return false
        }

        if (player == null) return false

        blockFace = face
        val nbt = CompoundTag()
            .putByte("SkullType", item.damage)
            .putByte("Rot", floor((player.rotation.yaw * 16 / 360) + 0.5).toInt() and 0x0f)
        if (item.hasCustomBlockData()) {
            for ((key, value) in item.customBlockData!!.entrySet) {
                nbt.put(key, value)
            }
        }
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
        // TODO: 2016/2/3 SPAWN WITHER
    }

    override fun onUpdate(type: Int): Int {
        if ((type != Level.BLOCK_UPDATE_REDSTONE && type != Level.BLOCK_UPDATE_NORMAL) || !Server.instance.settings.levelSettings()
                .enableRedstone()
        ) {
            return 0
        }

        val entity = blockEntity
        if (entity == null || entity.namedTag.getByte("SkullType").toInt() != 5) {
            return 0
        }

        val ev = RedstoneUpdateEvent(this)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return 0
        }

        entity.setMouthMoving(this.isGettingPower)
        return Level.BLOCK_UPDATE_REDSTONE
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override var blockFace: BlockFace
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        val bb: AxisAlignedBB = SimpleAxisAlignedBB(
            position.x + 0.25,
            position.y,
            position.z + 0.25,
            position.x + 1 - 0.25, position.y + 0.5, position.z + 1 - 0.25
        )
        return when (this.blockFace) {
            BlockFace.NORTH -> bb.offset(0.0, 0.25, 0.25)
            BlockFace.SOUTH -> bb.offset(0.0, 0.25, -0.25)
            BlockFace.WEST -> bb.offset(0.25, 0.25, 0.0)
            BlockFace.EAST -> bb.offset(-0.25, 0.25, 0.0)
            else -> bb
        }
    }
}
