package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntitySculkSensor.calPower
import org.chorus.blockentity.BlockEntitySculkSensor.power
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.utils.RedstoneComponent.updateAroundRedstone

/**
 * @author LT_Name
 */
class BlockSculkSensor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate), BlockEntityHolder<BlockEntitySculkSensor?>, RedstoneComponent {
    override val name: String
        get() = "Sculk Sensor"

    override val blockEntityClass: Class<out Any>
        get() = BlockEntitySculkSensor::class.java

    override fun getBlockEntityType(): String {
        return BlockEntity.SCULK_SENSOR

        override val isPowerSource: Boolean
        get() = true

        override fun getWeakPower(face: BlockFace): Int {
            val blockEntity: BlockEntitySculkSensor? = this.getOrCreateBlockEntity()
            return if (getSide(face.getOpposite()) is BlockRedstoneComparator) {
                BlockEntityID.COMPARATORPower
            } else {
                blockEntity.power
            }
        }

        override fun onUpdate(type: Int): Int {
            getOrCreateBlockEntity()
            if (type == Level.BLOCK_UPDATE_SCHEDULED) {
                if (Server.instance.settings.levelSettings().enableRedstone()) {
                    blockEntity.calPower()
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

        companion object {
            val properties: BlockProperties =
                BlockProperties(BlockID.SCULK_SENSOR, CommonBlockProperties.SCULK_SENSOR_PHASE)

        }
    }
