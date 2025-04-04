package org.chorus.block

import org.chorus.Server
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityID
import org.chorus.blockentity.BlockEntitySculkSensor
import org.chorus.level.Level
import org.chorus.level.Sound
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.utils.RedstoneComponent

class BlockSculkSensor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFlowable(blockstate), BlockEntityHolder<BlockEntitySculkSensor>, RedstoneComponent {
    override val name: String
        get() = "Sculk Sensor"

    override fun getBlockEntityClass() = BlockEntitySculkSensor::class.java

    override fun getBlockEntityType() = BlockEntityID.SCULK_SENSOR

    override val isPowerSource: Boolean
        get() = true

    override fun getWeakPower(face: BlockFace): Int {
        val blockEntity = this.getOrCreateBlockEntity()
        return if (getSide(face.getOpposite()) is BlockRedstoneComparator) {
            blockEntity.comparatorPower
        } else {
            blockEntity.power
        }
    }

    override fun onUpdate(type: Int): Int {
        getOrCreateBlockEntity()
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (Server.instance.settings.levelSettings.enableRedstone) {
                blockEntity!!.calPower()
                this.setPhase(0)
                updateAroundRedstone()
            }
            return type
        }
        return 0
    }

    fun setPhase(phase: Int) {
        if (phase == 1) level.addSound(position.add(0.5, 0.5, 0.5), Sound.POWER_ON_SCULK_SENSOR)
        else level.addSound(position.add(0.5, 0.5, 0.5), Sound.POWER_OFF_SCULK_SENSOR)
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

    override fun recalculateBoundingBox(): AxisAlignedBB {
        return this
    }

    override val waterloggingLevel: Int
        get() = 1

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SCULK_SENSOR, CommonBlockProperties.SCULK_SENSOR_PHASE)
    }
}
