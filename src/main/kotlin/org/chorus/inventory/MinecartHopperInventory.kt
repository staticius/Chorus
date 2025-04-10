package org.chorus.inventory

import org.chorus.entity.item.EntityHopperMinecart


class MinecartHopperInventory(minecart: EntityHopperMinecart) :
    ContainerInventory(minecart, InventoryType.MINECART_HOPPER, 5) {
}
