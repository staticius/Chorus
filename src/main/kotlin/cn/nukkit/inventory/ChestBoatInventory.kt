package cn.nukkit.inventory

import cn.nukkit.entity.item.EntityChestBoat

class ChestBoatInventory(holder: EntityChestBoat?) : ContainerInventory(holder, InventoryType.CHEST_BOAT, 27) {
    override var holder: InventoryHolder?
        get() = super.getHolder() as EntityChestBoat
        set(holder) {
            super.holder = holder
        }
}
