package cn.nukkit.inventory

import cn.nukkit.entity.item.EntityHopperMinecart


class MinecartHopperInventory(minecart: EntityHopperMinecart?) :
    ContainerInventory(minecart, InventoryType.MINECART_HOPPER, 5) {
    override var holder: InventoryHolder?
        get() = super.getHolder() as EntityHopperMinecart
        set(holder) {
            super.holder = holder
        }
}
