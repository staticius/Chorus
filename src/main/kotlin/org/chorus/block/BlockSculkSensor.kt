package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntitySculkSensor.calPower
import cn.nukkit.blockentity.BlockEntitySculkSensor.comparatorPower
import cn.nukkit.blockentity.BlockEntitySculkSensor.power
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.utils.RedstoneComponent.updateAroundRedstone

/**
 * @author LT_Name
 */
class BlockSculkSensor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate), BlockEntityHolder<BlockEntitySculkSensor?>, RedstoneComponent {
    override val name: String
        get() = "Sculk Sensor"

    override val blockEntityClass: Class<out Any>
        get() = BlockEntitySculkSensor::class.java

    override val blockEntityType: String
        get() = BlockEntity.SCULK_SENSOR

    override val isPowerSource: Boolean
        get() = true

    override fun getStrongPower(side: BlockFace?): Int {
        return super.getStrongPower(side)
    }

    override fun getWeakPower(face: BlockFace): Int {
        val blockEntity: BlockEntitySculkSensor? = this.orCreateBlockEntity
        return if (getSide(face.getOpposite()!!) is BlockRedstoneComparator) {
            blockEntity.comparatorPower
        } else {
            blockEntity.power
        }
    }

    override fun onUpdate(type: Int): Int {
        orCreateBlockEntity
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (level.server.settings.levelSettings().enableRedstone()) {
                blockEntity.calPower()
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
        val properties: BlockProperties =
            BlockProperties(BlockID.SCULK_SENSOR, CommonBlockProperties.SCULK_SENSOR_PHASE)
            get() = Companion.field
    }
}
