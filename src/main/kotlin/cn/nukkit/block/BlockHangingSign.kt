package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntityHangingSign
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.math.CompassRoseDirection
import cn.nukkit.math.CompassRoseDirection.Companion.from
import cn.nukkit.nbt.tag.CompoundTag
import lombok.extern.slf4j.Slf4j
import kotlin.math.floor

@Slf4j
abstract class BlockHangingSign(blockState: BlockState?) : BlockSignBase(blockState),
    BlockEntityHolder<BlockEntityHangingSign?> {
    override val blockEntityClass: Class<out BlockEntityHangingSign>
        get() = BlockEntityHangingSign::class.java

    override val blockEntityType: String
        get() = BlockEntity.HANGING_SIGN

    override val boundingBox: AxisAlignedBB?
        get() = null //01 23 45

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (isHanging) {
                if (up()!!.isAir) {
                    level.useBreakOn(this.position)
                    return Level.BLOCK_UPDATE_NORMAL
                }
            } else {
                if (checkGroundBlock() == null) {
                    level.useBreakOn(this.position)
                    return Level.BLOCK_UPDATE_NORMAL
                }
            }
        }
        return 0
    }

    val isHanging: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.HANGING)

    val isAttached: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.ATTACHED_BIT)

    override fun getSignDirection(): CompassRoseDirection? {
        return if (isHanging && isAttached) {
            from(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROUND_SIGN_DIRECTION))
        } else {
            fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))!!.compassRoseDirection
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
        var face = face
        if (player != null && !player.isSneaking() && target is BlockSignBase) {
            return false
        }
        if (face == BlockFace.UP) {
            val blockFace = checkGroundBlock() ?: return false
            face = blockFace
        }
        if (target is BlockHangingSign && face != BlockFace.DOWN) {
            return false
        }

        val layer0 = level.getBlock(this.position, 0)
        val layer1 = level.getBlock(this.position, 1)

        val nbt = CompoundTag()

        if (face == BlockFace.DOWN) {
            this.setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.HANGING, true)
            val direction = from(
                floor((((player?.rotation?.yaw ?: 0.0) + 180) * 16 / 360) + 0.5).toInt() and 0x0f
            )
            if ((player != null && player.isSneaking()) || target is BlockThin || target is BlockChain || target is BlockHangingSign) {
                this.setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.ATTACHED_BIT, true)
                this.setPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.GROUND_SIGN_DIRECTION,
                    direction.index
                )
                level.setBlock(block.position, this, true)
            } else {
                this.setPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.FACING_DIRECTION,
                    direction.closestBlockFace.index
                )
                level.setBlock(block.position, this, true)
            }
        } else {
            this.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.rotateY().index)
            level.setBlock(block.position, this, true)
        }
        if (item.hasCustomBlockData()) {
            for ((key, value) in item.customBlockData!!.entrySet) {
                nbt.put(key, value)
            }
        }

        try {
            createBlockEntity(nbt)
            player?.openSignEditor(this.position, true)
            return true
        } catch (e: Exception) {
            BlockHangingSign.log.warn("Failed to create block entity {} at {}", blockEntityType, locator, e)
            level.setBlock(layer0!!.position, 0, layer0, true)
            level.setBlock(layer1!!.position, 0, layer1, true)
            return false
        }
    }

    private fun checkGroundBlock(): BlockFace? {
        if (getSide(BlockFace.NORTH, 1)!!.canBePlaced()) return BlockFace.NORTH
        if (getSide(BlockFace.SOUTH, 1)!!.canBePlaced()) return BlockFace.SOUTH
        if (getSide(BlockFace.WEST, 1)!!.canBePlaced()) return BlockFace.WEST
        if (getSide(BlockFace.EAST, 1)!!.canBePlaced()) return BlockFace.EAST
        return null
    }
}
