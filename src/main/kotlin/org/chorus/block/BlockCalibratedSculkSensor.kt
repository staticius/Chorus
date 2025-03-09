package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.CommonPropertyMap
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntityCalibratedSculkSensor
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromHorizontalIndex
import cn.nukkit.utils.RedstoneComponent

class BlockCalibratedSculkSensor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate), BlockEntityHolder<BlockEntityCalibratedSculkSensor?>, RedstoneComponent {
    override val name: String
        get() = "Calibrated Sculk Sensor"

    override fun getBlockEntityClass(): Class<out BlockEntityCalibratedSculkSensor> {
        return BlockEntityCalibratedSculkSensor::class.java
    }

    override val isPowerSource: Boolean
        get() = true

    override fun getBlockEntityType(): String {
        return BlockEntity.CALIBRATED_SCULK_SENSOR
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
        blockFace =
            if (player != null) fromHorizontalIndex(player.getDirection()!!.horizontalIndex) else BlockFace.SOUTH

        level.setBlock(block.position, this, true, true)
        return true
    }

    var blockFace: BlockFace?
        get() = CommonPropertyMap.CARDINAL_BLOCKFACE[getPropertyValue(
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        )]
        set(face) {
            val horizontalIndex = face!!.horizontalIndex
            if (horizontalIndex > -1) {
                this.setPropertyValue(
                    CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
                    CommonPropertyMap.CARDINAL_BLOCKFACE.inverse()[fromHorizontalIndex(horizontalIndex)]
                )
            }
        }

    override fun getStrongPower(side: BlockFace?): Int {
        return super.getStrongPower(side)
    }

    override fun getWeakPower(face: BlockFace): Int {
        val blockEntity = this.getOrCreateBlockEntity()
        return if (getSide(face.getOpposite()!!) is BlockRedstoneComparator) {
            blockEntity.comparatorPower
        } else {
            blockEntity.power
        }
    }

    override fun onUpdate(type: Int): Int {
        getOrCreateBlockEntity()
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (level.server.settings.levelSettings().enableRedstone()) {
                this.blockEntity!!.calPower()
                this.setPhase(0)
                updateAroundRedstone()
            }
            return type
        }
        return 0
    }

    fun setPhase(phase: Int) {
        if (phase == 1) level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.POWER_ON_SCULK_SENSOR)
        else level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.POWER_OFF_SCULK_SENSOR)
        this.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.SCULK_SENSOR_PHASE, phase)
        level.setBlock(this.position, this, true, false)
    }

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun canPassThrough(): Boolean {
        return false
    }

    override fun breaksWhenMoved(): Boolean {
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
        val properties: BlockProperties = BlockProperties(
            CALIBRATED_SCULK_SENSOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.SCULK_SENSOR_PHASE
        )
            get() = Companion.field
    }
}