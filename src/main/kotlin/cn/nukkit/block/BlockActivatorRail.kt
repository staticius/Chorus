package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import cn.nukkit.utils.OptionalBoolean
import cn.nukkit.utils.Rail
import cn.nukkit.utils.Rail.Orientation.Companion.byMetadata
import cn.nukkit.utils.Rail.isRailBlock
import cn.nukkit.utils.RedstoneComponent
import cn.nukkit.utils.RedstoneComponent.Companion.updateAroundRedstone

class BlockActivatorRail @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockRail(blockstate), RedstoneComponent {
    override val name: String
        get() = "Activator Rail"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (super.onUpdate(type) == Level.BLOCK_UPDATE_NORMAL) {
                return 0 // Already broken
            }

            if (!level.server.settings.levelSettings().enableRedstone()) {
                return 0
            }

            val wasPowered = isActive
            val isPowered = this.isGettingPower
                    || checkSurrounding(this.position, true, 0)
                    || checkSurrounding(this.position, false, 0)

            // Avoid Block mistake
            if (wasPowered != isPowered) {
                isActive = isPowered
                updateAroundRedstone(down()!!, BlockFace.UP)
                if (orientation!!.isAscending) {
                    updateAroundRedstone(up()!!, BlockFace.DOWN)
                }
            }
            return type
        }
        return 0
    }

    /**
     * Check the surrounding of the rail
     *
     * @param pos      The rail position
     * @param relative The relative of the rail that will be checked
     * @param power    The count of the rail that had been counted
     * @return Boolean of the surrounding area. Where the powered rail on!
     */
    protected fun checkSurrounding(pos: Vector3, relative: Boolean, power: Int): Boolean {
        if (power >= 8) {
            return false
        }

        var dx = pos.floorX
        var dy = pos.floorY
        var dz = pos.floorZ

        val block: BlockRail
        val block2 = level.getBlock(Vector3(dx.toDouble(), dy.toDouble(), dz.toDouble()))

        if (isRailBlock(block2!!)) {
            block = block2 as BlockRail
        } else {
            return false
        }

        var base: Rail.Orientation? = null
        var onStraight = true

        when (block.orientation) {
            Rail.Orientation.STRAIGHT_NORTH_SOUTH -> {
                if (relative) {
                    dz++
                } else {
                    dz--
                }
            }

            Rail.Orientation.STRAIGHT_EAST_WEST -> {
                if (relative) {
                    dx--
                } else {
                    dx++
                }
            }

            Rail.Orientation.ASCENDING_EAST -> {
                if (relative) {
                    dx--
                } else {
                    dx++
                    dy++
                    onStraight = false
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST
            }

            Rail.Orientation.ASCENDING_WEST -> {
                if (relative) {
                    dx--
                    dy++
                    onStraight = false
                } else {
                    dx++
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST
            }

            Rail.Orientation.ASCENDING_NORTH -> {
                if (relative) {
                    dz++
                } else {
                    dz--
                    dy++
                    onStraight = false
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH
            }

            Rail.Orientation.ASCENDING_SOUTH -> {
                if (relative) {
                    dz++
                    dy++
                    onStraight = false
                } else {
                    dz--
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH
            }

            else -> {
                return false
            }
        }

        return canPowered(Vector3(dx.toDouble(), dy.toDouble(), dz.toDouble()), base, power, relative)
                || onStraight && canPowered(Vector3(dx.toDouble(), dy - 1.0, dz.toDouble()), base, power, relative)
    }

    protected fun canPowered(pos: Vector3, state: Rail.Orientation?, power: Int, relative: Boolean): Boolean {
        val block = level.getBlock(pos) as? BlockActivatorRail ?: return false

        val base = block.orientation

        return (state != Rail.Orientation.STRAIGHT_EAST_WEST
                || base != Rail.Orientation.STRAIGHT_NORTH_SOUTH && base != Rail.Orientation.ASCENDING_NORTH && base != Rail.Orientation.ASCENDING_SOUTH)
                && (state != Rail.Orientation.STRAIGHT_NORTH_SOUTH
                || base != Rail.Orientation.STRAIGHT_EAST_WEST && base != Rail.Orientation.ASCENDING_EAST && base != Rail.Orientation.ASCENDING_WEST)
                && (block.isGettingPower || checkSurrounding(pos, relative, power + 1))
    }

    override fun isActive(): Boolean {
        return getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.RAIL_DATA_BIT)
    }

    override fun isRailActive(): OptionalBoolean {
        return OptionalBoolean.of(getPropertyValue(CommonBlockProperties.RAIL_DATA_BIT))
    }

    override fun setRailActive(active: Boolean) {
        setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.RAIL_DATA_BIT, active)
    }

    override val hardness: Double
        get() = 0.5

    /**
     * Changes the rail direction.
     *
     * @param orientation The new orientation
     */
    override fun setRailDirection(orientation: Rail.Orientation) {
        setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.RAIL_DIRECTION_6, orientation.metadata())
    }

    override fun getOrientation(): Rail.Orientation? {
        return byMetadata(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.RAIL_DIRECTION_6))
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(ACTIVATOR_RAIL, CommonBlockProperties.RAIL_DATA_BIT, CommonBlockProperties.RAIL_DIRECTION_6)
            get() = Companion.field
    }
}