package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity
import org.chorus.item.Item
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.CompassRoseDirection.Companion.from
import org.chorus.nbt.tag.CompoundTag.put

import kotlin.math.floor

/**
 * Alias post sign
 */

open class BlockStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSignBase(blockstate), BlockEntityHolder<BlockEntitySign?> {
    protected open val standingSignId: String?
        get() = id

    open val wallSignId: String
        get() = BlockID.WALL_SIGN

    override val blockEntityClass: Class<out Any>
        get() = BlockEntitySign::class.java

    override val blockEntityType: String
        get() = BlockEntity.SIGN

    override val boundingBox: AxisAlignedBB?
        get() = null

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.isAir) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        }
        return 0
    }

    override fun toItem(): Item? {
        return ItemOakSign()
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
        if (player != null && !player.isSneaking() && target is BlockSignBase) {
            return false
        }

        val layer0 = level.getBlock(this.position, 0)
        val layer1 = level.getBlock(this.position, 1)

        val nbt: CompoundTag = CompoundTag()

        if (face == BlockFace.UP) {
            val direction: CompassRoseDirection = CompassRoseDirection.from(
                floor(
                    (((player?.rotation?.yaw
                        ?: 0.0) + 180) * 16 / 360) + 0.5
                ).toInt() and 0x0f
            )
            val post = get(standingSignId!!)
            post.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROUND_SIGN_DIRECTION, direction.index)
            level.setBlock(block.position, post, true)
        } else {
            val wall = get(wallSignId)
            wall.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
            level.setBlock(block.position, wall, true)
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
            BlockStandingSign.log.warn("Failed to create block entity {} at {}", blockEntityType, locator, e)
            level.setBlock(layer0!!.position, 0, layer0, true)
            level.setBlock(layer1!!.position, 0, layer1, true)
            return false
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}