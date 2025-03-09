package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.blockentity.*
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.MathHelper.clamp
import org.chorus.math.MathHelper.cos
import org.chorus.utils.RedstoneComponent

/**
 * @author CreeperFace
 * @since 2015/11/22
 */
open class BlockDaylightDetector @JvmOverloads constructor(state: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(state), RedstoneComponent, BlockEntityHolder<BlockEntityDaylightDetector?> {
    override val name: String
        get() = "Daylight Detector"

    override fun getBlockEntityType(): String {
        return BlockEntity.DAYLIGHT_DETECTOR
    }

    override fun getBlockEntityClass(): Class<out BlockEntityDaylightDetector> {
        return BlockEntityDaylightDetector::class.java
    }

    override val hardness: Double
        get() = 0.2

    override val waterloggingLevel: Int
        get() = 1

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun toItem(): Item? {
        return ItemBlock(this, 0)
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onUpdate(type: Int): Int {
        if (!level.server.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
            val be = blockEntity
            be?.scheduleUpdate()
        }
        return type
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
        val detector =
            BlockEntityHolder.setBlockAndCreateEntity(
                this
            )
                ?: return false
        if (level.dimension == Level.DIMENSION_OVERWORLD) {
            if (level.server.settings.levelSettings().enableRedstone()) {
                updatePower()
            }
        }
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false
        val block = BlockDaylightDetectorInverted()
        level.setBlock(this.position, block, true, true)
        if (level.server.settings.levelSettings().enableRedstone()) {
            block.updatePower()
        }
        return true
    }

    override fun onBreak(item: Item?): Boolean {
        if (super.onBreak(item)) {
            if (level.dimension == Level.DIMENSION_OVERWORLD) {
                updateAroundRedstone()
            }
            return true
        }
        return false
    }

    override fun getWeakPower(face: BlockFace?): Int {
        return level.getBlockStateAt(floorX, floorY, floorZ)!!.getPropertyValue(CommonBlockProperties.REDSTONE_SIGNAL)
    }

    override val isPowerSource: Boolean
        get() = true

    open val isInverted: Boolean
        get() = false

    fun updatePower() {
        var i: Int
        if (level.dimension == Level.DIMENSION_OVERWORLD) {
            i = level.getBlockSkyLightAt(
                position.floorX,
                position.floorY, position.floorZ
            ) - level.calculateSkylightSubtracted(1.0f)
            var f = level.getCelestialAngle(1.0f) * 6.2831855f

            if (this.isInverted) {
                i = 15 - i
            }

            if (i > 0 && !this.isInverted) {
                val f1 = if (f < Math.PI.toFloat()) 0.0f else (Math.PI.toFloat() * 2f)
                f = f + (f1 - f) * 0.2f
                i = Math.round(i.toFloat() * cos(f))
            }

            i = clamp(i, 0, 15)
        } else i = 0

        if (i != level.getBlockStateAt(floorX, floorY, floorZ)!!
                .getPropertyValue(CommonBlockProperties.REDSTONE_SIGNAL)
        ) {
            this.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.REDSTONE_SIGNAL, i)
            val blockState = blockState
            level.setBlockStateAt(floorX, floorY, floorZ, blockState)
            updateAroundRedstone()
        }
    }

    override val isSolid: Boolean
        get() = false

    override var maxY: Double
        get() = position.y + 0.625
        set(maxY) {
            super.maxY = maxY
        }

    companion object {
        val properties: BlockProperties = BlockProperties(DAYLIGHT_DETECTOR, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}
