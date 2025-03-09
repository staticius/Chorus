package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.entity.item.EntityMinecartAbstract
import cn.nukkit.inventory.ContainerInventory.Companion.calculateRedstone
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.math.BlockFace
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.utils.OptionalBoolean
import cn.nukkit.utils.Rail
import cn.nukkit.utils.Rail.Orientation.Companion.byMetadata
import cn.nukkit.utils.RedstoneComponent
import cn.nukkit.utils.RedstoneComponent.Companion.updateAroundRedstone

class BlockDetectorRail @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockRail(blockstate), RedstoneComponent {
    override val name: String
        get() = "Detector Rail"

    override val isPowerSource: Boolean
        get() = true

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() = if (findMinecart() is InventoryHolder) calculateRedstone(
            inventoryHolder.inventory
        ) else 0

    override fun getWeakPower(side: BlockFace?): Int {
        return if (isActive) 15 else 0
    }

    override fun getStrongPower(side: BlockFace?): Int {
        return if (isActive) (if (side == BlockFace.UP) 15 else 0) else 0
    }

    fun findMinecart(): EntityMinecartAbstract? {
        for (entity in level.getNearbyEntities(
            SimpleAxisAlignedBB(
                floorX + 0.2,
                floorY.toDouble(),
                floorZ + 0.2,
                floorX + 0.8,
                floorY + 0.8,
                floorZ + 0.8
            )
        )) {
            if (entity is EntityMinecartAbstract) return entity
        }
        return null
    }

    fun updateState(powered: Boolean) {
        val wasPowered = isActive
        if (powered != wasPowered) {
            this.isActive = powered
            updateAroundRedstone()
            updateAroundRedstone(getSide(BlockFace.DOWN)!!)
        }
        if (powered) {
            //每20gt检查一遍
            level.scheduleUpdate(this, 20)
            //更新比较器输出
            level.updateComparatorOutputLevel(this.position)
        }
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
            BlockProperties(DETECTOR_RAIL, CommonBlockProperties.RAIL_DATA_BIT, CommonBlockProperties.RAIL_DIRECTION_6)
            get() = Companion.field
    }
}