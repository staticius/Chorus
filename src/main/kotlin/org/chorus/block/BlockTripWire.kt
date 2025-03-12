package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.entity.Entity
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace

class BlockTripWire @JvmOverloads constructor(state: BlockState? = Companion.properties.getDefaultState()) :
    BlockTransparent(state) {
    override val name: String
        get() = "Tripwire"

    override fun canPassThrough(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override val resistance: Double
        get() = 0.0

    override val hardness: Double
        get() = 0.0

    override val boundingBox: AxisAlignedBB?
        get() = null

    override fun toItem(): Item {
        return ItemString()
    }

    override val itemId: String
        get() = ItemID.STRING

    var isPowered: Boolean
        get() = this.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.POWERED_BIT)
        set(isPowered) {
            if (field == isPowered) {
                return
            }
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.POWERED_BIT,
                isPowered
            )
        }

    var isAttached: Boolean
        get() = this.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.ATTACHED_BIT)
        set(isAttached) {
            if (field == isAttached) {
                return
            }
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.ATTACHED_BIT,
                isAttached
            )
        }

    var isSuspended: Boolean
        get() = this.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.SUSPENDED_BIT)
        set(isSuspended) {
            if (field == isSuspended) {
                return
            }
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.SUSPENDED_BIT,
                isSuspended
            )
        }

    var isDisarmed: Boolean
        get() = this.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.DISARMED_BIT)
        set(isDisarmed) {
            if (field == isDisarmed) {
                return
            }
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.DISARMED_BIT,
                isDisarmed
            )
        }

    override fun onEntityCollide(entity: Entity) {
        if (!Server.instance.settings.levelSettings().enableRedstone()) {
            return
        }
        if (!entity.doesTriggerPressurePlate()) {
            return
        }
        if (this.isPowered) {
            return
        }

        this.isPowered = true
        level.setBlock(this.position, this, true, false)
        this.updateHook(false)

        level.scheduleUpdate(this, 10)
        level.updateComparatorOutputLevelSelective(this.position, true)
    }

    private fun updateHook(scheduleUpdate: Boolean) {
        if (!Server.instance.settings.levelSettings().enableRedstone()) {
            return
        }

        for (side in arrayOf<BlockFace>(BlockFace.SOUTH, BlockFace.WEST)) {
            for (i in 1..<BlockTripwireHook.Companion.MAX_TRIPWIRE_CIRCUIT_LENGTH) {
                val block = this.getSide(side, i)

                if (block is BlockTripwireHook) {
                    if (block.facing == side.getOpposite()) {
                        block.updateLine(false, true, i, this)
                    }

                    /*if(scheduleUpdate) {
                        this.level.scheduleUpdate(hook, 10);
                    }*/
                    break
                }

                if (block !is BlockTripWire) {
                    break
                }
            }
        }
    }

    override fun onUpdate(type: Int): Int {
        if (!Server.instance.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!isPowered) {
                return type
            }

            for (entity in level.getCollidingEntities(collisionBoundingBox!!)) {
                if (!entity.doesTriggerPressurePlate()) {
                    continue
                }
                level.scheduleUpdate(this, 10)
                return type
            }

            this.isPowered = false
            level.setBlock(this.position, this, true, false)
            this.updateHook(false)

            level.updateComparatorOutputLevelSelective(this.position, true)

            return type
        }

        return 0
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
        level.setBlock(this.position, this, true, true)
        this.updateHook(false)

        return true
    }

    override fun onBreak(item: Item?): Boolean {
        if (item is ItemShears) {
            this.isDisarmed = true
            level.setBlock(this.position, this, true, false)
            this.updateHook(false)
            level.setBlock(this.position, get(BlockID.AIR), true, true)
            //todo: initiator should be a entity
            level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    this, position.add(0.5, 0.5, 0.5), VibrationType.SHEAR
                )
            )
            return true
        }

        this.isPowered = true
        level.setBlock(this.position, get(BlockID.AIR), true, true)
        this.updateHook(true)

        return true
    }

    override var maxY: Double
        get() = position.y + 0.5
        set(maxY) {
            super.maxY = maxY
        }

    override fun recalculateCollisionBoundingBox(): AxisAlignedBB {
        return this
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.TRIP_WIRE,
            CommonBlockProperties.POWERED_BIT,
            CommonBlockProperties.SUSPENDED_BIT,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.DISARMED_BIT
        )

    }
}
