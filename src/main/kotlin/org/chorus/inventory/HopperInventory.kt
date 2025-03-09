package org.chorus.inventory

import org.chorus.blockentity.BlockEntityHopper
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author CreeperFace
 * @since 8.5.2017
 */
class HopperInventory(hopper: BlockEntityHopper?) : ContainerInventory(hopper, InventoryType.HOPPER, 5) {
    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map!![i] = ContainerSlotType.LEVEL_ENTITY
        }
    }

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        val map = super.slotTypeMap()
        for (i in 0..<this.getSize()) {
            map!![i] = ContainerSlotType.INVENTORY
        }
        return map!!
    }

    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockEntityHopper
        set(holder) {
            super.holder = holder
        }

    override fun canCauseVibration(): Boolean {
        return true
    }
}
