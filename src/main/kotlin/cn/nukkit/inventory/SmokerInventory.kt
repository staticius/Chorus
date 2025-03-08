package cn.nukkit.inventory

import cn.nukkit.blockentity.BlockEntityFurnace
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

class SmokerInventory(furnace: BlockEntityFurnace?) : SmeltingInventory(furnace, InventoryType.SMOKER, 3) {
    override fun init() {
        val map = super.slotTypeMap()
        map!![0] = ContainerSlotType.SMOKER_INGREDIENT
        map[1] = ContainerSlotType.FURNACE_FUEL
        map[2] = ContainerSlotType.FURNACE_RESULT
    }
}
